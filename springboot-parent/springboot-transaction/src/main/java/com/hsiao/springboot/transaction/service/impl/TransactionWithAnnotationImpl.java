package com.hsiao.springboot.transaction.service.impl;

import com.hsiao.springboot.transaction.dao.AccountDao;
import com.hsiao.springboot.transaction.entity.Account;
import com.hsiao.springboot.transaction.service.AbstractAccountService;
import com.hsiao.springboot.transaction.service.AccountService;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 属性名	    说明
 * name	        当在配置文件中有多个 TransactionManager , 可以用该属性指定选择哪个事务管理器。
 * propagation	事务的传播行为，默认值为 REQUIRED。
 * isolation	事务的隔离度，默认值采用 DEFAULT。
 * timeout	    事务的超时时间，默认值为-1。如果超过该时间限制但事务还没有完成，则自动回滚事务。
 * read-only	指定事务是否为只读事务，默认值为 false；为了忽略那些不需要事务的方法，比如读取数据，可以设置 read-only 为 true。
 * rollback-for	用于指定能够触发事务回滚的异常类型，如果有多个异常类型需要指定，各类型之间可以通过逗号分隔。
 * no-rollback- for	抛出 no-rollback-for 指定的异常类型，不回滚事务。
 */
@Component("transactionWithAnnotation")
@Getter
@Setter
public class TransactionWithAnnotationImpl extends AbstractAccountService implements AccountService {

    @Autowired()
    private AccountDao accountDao;

    @Override
    public List<Account> getAllList() {
        return accountDao.getAllList();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = RuntimeException.class/*, timeout = 3*/)
    @Override
    public void transfer(String out, String in, Double money) {
        System.out.println("annotation事务-转账开始");
        System.out.println(String.format("用户ID：%s 给用户ID：%s 转账金额：%s，是否模拟异常：%s", out, in, money, needException));
        accountDao.outMoney(out, money);
        if (needException) {
            throw new RuntimeException("annotation事务-转账异常");
        }
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("annotation-转账成功！");
        accountDao.inMoney(in, money);
    }

    @Override
    public Double query(String name) {
        return accountDao.query(name);
    }
}
