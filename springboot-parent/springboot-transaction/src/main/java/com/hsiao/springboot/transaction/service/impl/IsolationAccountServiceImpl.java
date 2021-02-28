package com.hsiao.springboot.transaction.service.impl;

import com.hsiao.springboot.transaction.dao.AccountDao;
import com.hsiao.springboot.transaction.entity.Account;
import com.hsiao.springboot.transaction.service.AbstractAccountService;
import com.hsiao.springboot.transaction.service.IsolationAccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hsiao
 * @date 2019/1/24
 */
@Component
public class IsolationAccountServiceImpl extends AbstractAccountService implements IsolationAccountService {

    @Autowired()
    private AccountDao accountDao;

    @Override
    public void insert(Account account) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "添加用户账户信息：" + account);
        accountDao.insert(account);
        System.out.println(threadName + "添加用户账户成功");
    }

    @Override
    public void delete(int id) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "删除用户账户信息，id=" + id);
        accountDao.delete(id);
        System.out.println(threadName + "删除用户账户成功");
    }

    @Override
    public void update(Account account) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "更新用户账户信息，id=" + account);
        accountDao.update(account);
        System.out.println(threadName + "更新用户账户成功");
    }

    // ---------- ru 事物隔离级别
    // 测试脏读，会读取到另外一个事务未提交的修改
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED, rollbackFor = Exception.class)
    public boolean readRuTransaction(String name) throws InterruptedException {
        accountDao.query(name);
        Thread.sleep(1000);
        accountDao.query(name);
        return true;
    }

    /**
     * ru隔离级别的事务，可能出现脏读，不可避免不可重复读，幻读
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = RuntimeException.class)
    @Override
    public void readUncommittedTest() {
        readUncommitted_insert(new Account("uncommitted", 1000));
        sleep(10 * 1000);
        System.out.println(1 / 0);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = RuntimeException.class)
    void readUncommitted_insert(Account account) {
        insert(account);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = RuntimeException.class)
    @Override
    public void getReadUncommittedAllList() {
        try {
            int i = 0;
            while (i++ < 3) {
                System.out.println(Thread.currentThread().getName() + "获取用户账户信息列表start=========");
                Thread.sleep(5000);
                List<Account> beforeList = getAllList();
                for (Account account : beforeList) {
                    System.out.println(account);
                }
                System.out.println(Thread.currentThread().getName() + "获取用户账户信息列表end=========");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // ---------- rc 事物隔离级别
    // 测试不可重复读，一个事务内，两次读取的结果不一样
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean readRcTransaction(String name) throws InterruptedException {
        this.query(name);
        Thread.sleep(1000);
        this.query(name);
        Thread.sleep(3000);
        this.query(name);
        return true;
    }

    /**
     * rc隔离级别事务，未提交的写事务，会挂起其他的读写事务；可避免脏读，更新丢失；但不能防止不可重复读、幻读
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
    @Override
    public void readCommittedTest() {
        readcommitted_insert(new Account("committed", 2000));
        sleep(10 * 1000);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
    void readcommitted_insert(Account account) {
        insert(account);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
    @Override
    public void getReadCommittedAllList() {
        try {
            int i = 0;
            while (i++ < 3) {
                System.out.println(Thread.currentThread().getName() + "获取用户账户信息列表start=========");
                Thread.sleep(5000);
                List<Account> beforeList = getAllList();
                for (Account account : beforeList) {
                    System.out.println(account);
                }
                System.out.println(Thread.currentThread().getName() + "获取用户账户信息列表end=========");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- rr 事物隔离级别

    /**
     * 只读事务，主要目的是为了隔离其他事务的修改，对本次操作的影响；
     *
     * 比如在某些耗时的涉及多次表的读取操作中，为了保证数据一致性，这个就有用了； 开启只读事务之后，不支持修改数据
     *
     * rr隔离级别事务，读事务禁止其他的写事务，未提交写事务，会挂起其他读写事务；可避免脏读，不可重复读，（我个人认为，innodb引擎可通过mvvc+gap锁避免幻读）
     */
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ, rollbackFor = RuntimeException.class)
    @Override
    public void repeatableReadTest() {
        repeatable_insert(new Account("repeatable", 3000));
        sleep(10 * 1000);
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = RuntimeException.class)
    void repeatable_insert(Account account) {
        insert(account);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = RuntimeException.class)
    public void getRepeatableAllList() {
        try {
            int i = 0;
            while (i++ < 3) {
                System.out.println(Thread.currentThread().getName() + "获取用户账户信息列表start=========");
                Thread.sleep(5000);
                List<Account> beforeList = getAllList();
                for (Account account : beforeList) {
                    System.out.println(account);
                }
                System.out.println(Thread.currentThread().getName() + "获取用户账户信息列表end=========");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = RuntimeException.class)
    @Override
    public void serializableTest() {
        serializable_insert(new Account("serializable", 4000));
        sleep(10 * 1000);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = RuntimeException.class)
    void serializable_insert(Account account) {
        insert(account);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = RuntimeException.class)
    public void getSerializableAllList() {
        try {
            int i = 0;
            while (i++ < 3) {
                System.out.println(Thread.currentThread().getName() + "获取用户账户信息列表start=========");
                Thread.sleep(5000);
                List<Account> beforeList = getAllList();
                for (Account account : beforeList) {
                    System.out.println(account);
                }
                System.out.println(Thread.currentThread().getName() + "获取用户账户信息列表end=========");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void transfer(String out, String in, Double money) {

    }

    @Override
    public Double query(String name) {
        return null;
    }

    @Override
    public List<Account> getAllList() {
        return accountDao.getAllList();
    }


    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
