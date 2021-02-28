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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Component("accountTransactionTemplate")
@Getter
@Setter
public class TransactionTemplateImpl extends AbstractAccountService implements AccountService {

    @Autowired
    private AccountDao accountDao;

    private TransactionTemplate transactionTemplate;

    @Override
    public void transfer(final String out, final String in, final Double money) {
        System.out.println("transactionTemplate事务-转账开始");
        System.out.println(
                String.format("用户ID：%s 给用户ID：%s 转账金额：%s，是否模拟异常：%s", out, in, money, needException));
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                accountDao.outMoney(out, money);
                if (needException) {
                    int i = 10 / 0;
                }
//                if (needException) {
//                    throw new RuntimeException("拦截器事务-转账异常");
//                }
                accountDao.inMoney(in, money);
                System.out.println("transactionTemplate事务-转账成功！");
            }
        });
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public Double query(String name) {
        return null;
    }

    @Override
    public List<Account> getAllList() {
        return accountDao.getAllList();
    }

}
