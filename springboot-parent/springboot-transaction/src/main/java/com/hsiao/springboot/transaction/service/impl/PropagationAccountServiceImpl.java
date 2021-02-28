package com.hsiao.springboot.transaction.service.impl;

import com.hsiao.springboot.transaction.dao.AccountDao;
import com.hsiao.springboot.transaction.entity.Account;
import com.hsiao.springboot.transaction.service.AbstractAccountService;
import com.hsiao.springboot.transaction.service.PropagationAccountService;
import java.io.IOException;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Component
@Getter
@Setter
public class PropagationAccountServiceImpl extends AbstractAccountService implements PropagationAccountService {

    @Autowired()
    private AccountDao accountDao;

    @Autowired
    private TempService tempService;

    @Override
    public void insert(Account po) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "添加用户账户信息：" + po);
        accountDao.insert(po);
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
    public void update(Account po) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "更新用户账户信息，id=" + po);
        accountDao.update(po);
        System.out.println(threadName + "更新用户账户成功");
    }

    @Override
    public void transfer(String out, String in, Double money) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "转账开始");
        System.out.println(
                String.format("%s用户ID：%s 给用户ID：%s 转账金额：%s，是否模拟异常：%s", threadName, out, in, money,
                        needException));
        accountDao.outMoney(out, money);
        if (needException) {
            throw new RuntimeException(threadName + "转账异常");
        }
        accountDao.inMoney(in, money);
        System.out.println(threadName + "转账成功！");
    }

    @Override
    public Double query(String name) {
        return accountDao.query(name);
    }

    @Override
    public List<Account> getAllList() {
        return accountDao.getAllList();
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @Override
    public void propagationrequiredTest() {
        System.out.println("测试事务属性Propagation.REQUIRED开始！");
        insert(new Account("zhangsan", 100));
        System.out.println(1 / 0);
        insert(new Account("lisi", 200));
        System.out.println("测试事务属性Propagation.REQUIRED结束！");

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @Override
    public void propagationrequires_newTest() {
        System.out.println("测试事务属性Propagation.REQUIRES_NEW开始！");
        tempService.insert1(new Account("wangwu", 300));
        //同一个Service类中，spring并不重新创建新事务，如果是两不同的Service，就会创建新事务了。
        //跨Service调用方法时，都会经过org.springframework.aop.framework.CglibAopProxy.DynamicAdvisedInterceptor.intercept()方法，只有经过此处，才能对事务进行控制。
        //insert1(new Account("wangwu",300));
        System.out.println(1 / 0);
        System.out.println("测试事务属性Propagation.REQUIRES_NEW结束！");
    }

    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @Override
    public void propagationsupportsTest() {
        System.out.println("测试事务属性Propagation.SUPPORTS开始！");
        tempService.insert2(new Account("zhaoliu", 400));
        //同一个Service类中，spring并不重新创建新事务，如果是两不同的Service，就会创建新事务了。
        //跨Service调用方法时，都会经过org.springframework.aop.framework.CglibAopProxy.DynamicAdvisedInterceptor.intercept()方法，只有经过此处，才能对事务进行控制。
        //insert1(new Account("wangwu",300));
        System.out.println(1 / 0);
        System.out.println("测试事务属性Propagation.SUPPORTS结束！");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @Override
    public void propagationnot_supportedTest() {
        System.out.println("测试事务属性Propagation.NOT_SUPPORTED开始！");
        tempService.insert3(new Account("hanba", 600));
        //同一个Service类中，spring并不重新创建新事务，如果是两不同的Service，就会创建新事务了。
        //跨Service调用方法时，都会经过org.springframework.aop.framework.CglibAopProxy.DynamicAdvisedInterceptor.intercept()方法，只有经过此处，才能对事务进行控制。
        //insert1(new Account("wangwu",300));
        System.out.println(1 / 0);
        System.out.println("测试事务属性Propagation.NOT_SUPPORTED结束！");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @Override
    public void propagationmandatoryTest() {
        System.out.println("测试事务属性Propagation.MANDATORY开始！");
        tempService.insert4(new Account("zhouqiu", 700));
        System.out.println("测试事务属性Propagation.MANDATORY结束！");
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @Override
    public void propagationnestedTest() {
        System.out.println("测试事务属性Propagation.NESTED开始！");
        tempService.insert5(new Account("qianshi", 800));
        //System.out.println(1 / 0);
        System.out.println("测试事务属性Propagation.NESTED结束！");
    }

    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @Override
    public void propagationneverTest() {
        System.out.println("测试事务属性Propagation.NEVER开始！");
        tempService.insert6(new Account("songshiyi", 900));
        System.out.println("测试事务属性Propagation.NEVER结束！");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = IOException.class)
    public void insert1(Account po) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "添加用户账户信息：" + po);
        accountDao.insert(po);
        System.out.println(threadName + "添加用户账户成功");
    }

}

@Component
class TempService {

    @Autowired()
    private AccountDao accountDao;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RuntimeException.class)
    public void insert1(Account po) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "添加用户账户信息：" + po);
        accountDao.insert(po);
        System.out.println(threadName + "添加用户账户成功");
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = RuntimeException.class)
    public void insert2(Account po) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "添加用户账户信息：" + po);
        accountDao.insert(po);
        System.out.println(threadName + "添加用户账户成功");
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = RuntimeException.class)
    public void insert3(Account po) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "添加用户账户信息：" + po);
        accountDao.insert(po);
        System.out.println(threadName + "添加用户账户成功");
    }

    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = RuntimeException.class)
    public void insert4(Account po) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "添加用户账户信息：" + po);
        accountDao.insert(po);
        System.out.println(threadName + "添加用户账户成功");
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = RuntimeException.class)
    public void insert5(Account po) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "添加用户账户信息：" + po);
        accountDao.insert(po);
        System.out.println(threadName + "添加用户账户成功");
    }

    @Transactional(propagation = Propagation.NEVER, rollbackFor = RuntimeException.class)
    public void insert6(Account po) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "添加用户账户信息：" + po);
        accountDao.insert(po);
        System.out.println(threadName + "添加用户账户成功");
    }
}
