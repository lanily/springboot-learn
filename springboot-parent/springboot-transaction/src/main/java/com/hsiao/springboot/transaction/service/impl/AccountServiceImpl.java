package com.hsiao.springboot.transaction.service.impl;


import com.hsiao.springboot.transaction.dao.AccountDao;
import com.hsiao.springboot.transaction.entity.Account;
import com.hsiao.springboot.transaction.service.AbstractAccountService;
import com.hsiao.springboot.transaction.service.AccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: AccountServiceImpl
 * @description: TODO
 * @author xiao
 * @create 2021/2/27
 * @since 1.0.0
 */
@Service("accountTransactionQuery")  // @Component
public class AccountServiceImpl extends AbstractAccountService implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public void transfer(String out, String in, Double money) {
        accountDao.outMoney(out, money);
        if (needException) {
            int i = 10 / 0;
        }
        accountDao.inMoney(in, money);
    }

    @Override
    public Double query(String name) {
        return accountDao.query(name);
    }

    @Override
    public List<Account> getAllList() {
        return accountDao.getAllList();
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /*
    实现类中获取本类的代理对象，Spring提供了Aop上下文，即：AopContext，通过AopContext，可以很方便的获取到代理对象
    @Autowired
    @Qualifier("accountTransactionQuery")
    AccountService accountService;

    @Transactional
    public void updateAndQuery() {
        try {
            ((AccountService) AopContext.currentProxy()).updateMoney();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        accountDao.query("ccc");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateMoney() {
        accountDao.inMoney("ccc", 10d);
        int a = 1 / 0;
    }*/

}

