package com.hsiao.springboot.transaction.service;


import com.hsiao.springboot.transaction.dao.impl.NotEffectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * 不生效的demo
 *
 *
 * 1、数据库引擎不支持事务
 *
 * 这里以 MySQL 为例，其 MyISAM 引擎是不支持事务操作的，InnoDB 才是支持事务的引擎，一般要支持事务都会使用 InnoDB。
 * 根据 MySQL 的官方文档：https://dev.mysql.com/doc/refman/5.5/en/storage-engine-setting.html
 * 从 MySQL 5.5.5 开始的默认存储引擎是：InnoDB，之前默认的都是：MyISAM，所以这点要值得注意，底层引擎不支持事务再怎么搞都是白搭。
 * 2、没有被 Spring 管理
 * 如下面例子所示：
 * // @Service
 * public class OrderServiceImpl implements OrderService {
 *     @Transactional
 *     public void updateOrder(Order order) {
 *         // update order
 *     }
 * }
 * 如果此时把 @Service 注解注释掉，这个类就不会被加载成一个 Bean，那这个类就不会被 Spring 管理了，事务自然就失效了。
 *
 * 3、方法不是 public 的
 *
 * 以下来自 Spring 官方文档：
 *
 * When using proxies, you should apply the @Transactional annotation only to methods with public visibility. If you do annotate protected, private or package-visible methods with the @Transactional annotation, no error is raised, but the annotated method does not exhibit the configured transactional settings. Consider the use of AspectJ (see below) if you need to annotate non-public methods.
 * 大概意思就是 @Transactional 只能用于 public 的方法上，否则事务不会失效，如果要用在非 public 方法上，可以开启 AspectJ 代理模式。
 *
 * 4、自身调用问题
 * 因为它们发生了自身调用，就调该类自己的方法，而没有经过 Spring 的代理类，默认只有在外部调用事务才会生效
 *
 * 5、数据源没有配置事务管理器
 *
 * @Bean
 * public PlatformTransactionManager transactionManager(DataSource dataSource) {
 *     return new DataSourceTransactionManager(dataSource);
 * }
 * 如上面所示，当前数据源若没有配置事务管理器，那也是白搭！
 *
 * 6、不支持事务
 *
 * 来看下面这个例子：
 *来看下面这个例子：
 *
 * @Service
 * public class OrderServiceImpl implements OrderService {
 *     @Transactional
 *     public void update(Order order) {
 *         updateOrder(order);
 *     }
 *     @Transactional(propagation = Propagation.NOT_SUPPORTED)
 *     public void updateOrder(Order order) {
 *         // update order
 *     }
 * }
 * Propagation.NOT_SUPPORTED： 表示不以事务运行，当前若存在事务则挂起，详细的可以参考《事务隔离级别和传播机制》这篇文章。
 *
 * 都主动不支持以事务方式运行了，那事务生效也是白搭！
 *
 * 7、异常被吃了
 * 这个也是出现比较多的场景：
 * // @Service
 * public class OrderServiceImpl implements OrderService {
 *
 *     @Transactional
 *     public void updateOrder(Order order) {
 *         try {
 *             // update order
 *         } catch {
 *         }
 *     }
 *
 * }
 * 把异常吃了，然后又不抛出来，事务怎么回滚吧！
 *
 * 8、异常类型错误
 *
 * 上面的例子再抛出一个异常：
 *
 * // @Service
 * public class OrderServiceImpl implements OrderService {
 *     @Transactional
 *     public void updateOrder(Order order) {
 *         try {
 *             // update order
 *         } catch {
 *             throw new Exception("更新错误");
 *         }
 *     }
 *
 * }
 * 这样事务也是不生效的，因为默认回滚的是：RuntimeException，如果你想触发其他异常的回滚，需要在注解上配置一下，如：
 *
 * @Transactional(rollbackFor = Exception.class)
 * 这个配置仅限于 Throwable 异常类及其子类。
 *
 * https://cloud.tencent.com/developer/article/1581840
 *
 *
 * @projectName springboot-parent
 * @title: NotEffectService
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
@Service
public class NotEffectService {

    @Autowired
    NotEffectDao jdbcDao;

    /**
     * 私有方法上的注解，不生效
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Transactional
    private boolean testSpecialException(int id) throws Exception {
        if (jdbcDao.updateName(id)) {
            jdbcDao.query("after update name", id);
            if (jdbcDao.updateMoney(id)) {
                return true;
            }
        }

        throw new Exception("参数异常");
    }


    /**
     * 非运行异常，且没有通过 rollbackFor 指定抛出的异常，不生效
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Transactional
    public boolean testCompleException(int id) throws Exception {
        if (jdbcDao.updateName(id)) {
            jdbcDao.query("after update name", id);
            if (jdbcDao.updateMoney(id)) {
                return true;
            }
        }

        throw new Exception("参数异常");
    }

    public boolean testCall(int id) throws Exception {
        return testCompileException2(id);
    }

    /**
     * 非直接调用，不生效
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean testCompileException2(int id) throws Exception {
        if (jdbcDao.updateName(id)) {
            jdbcDao.query("after update name", id);
            if (jdbcDao.updateMoney(id)) {
                return true;
            }
        }

        throw new Exception("参数异常");
    }


    /**
     * 子线程抛异常，主线程无法捕获，导致事务不生效
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean testMultThread(int id) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                jdbcDao.updateName(id);
                jdbcDao.query("after update name", id);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ans = jdbcDao.updateMoney(id);
                jdbcDao.query("after update id", id);
                if (!ans) {
                    throw new RuntimeException("failed to update ans");
                }
            }
        }).start();

        Thread.sleep(1000);
        System.out.println("------- 子线程 --------");

        return true;
    }


    /**
     * 子线程抛异常，主线程无法捕获，导致事务不生效
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean testMultThread2(int id) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                jdbcDao.updateName(id);
                jdbcDao.query("after update name", id);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ans = jdbcDao.updateMoney(id);
                jdbcDao.query("after update id", id);
            }
        }).start();

        Thread.sleep(1000);
        System.out.println("------- 子线程 --------");

        jdbcDao.updateMoney(id);
        jdbcDao.query("after outer update id", id);
        throw new RuntimeException("failed to update ans");
    }

    public void query(String tag, int id) {
        jdbcDao.query(tag, id);
    }
}

