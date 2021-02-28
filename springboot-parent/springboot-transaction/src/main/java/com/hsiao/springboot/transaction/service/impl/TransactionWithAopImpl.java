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

@Component("transactionWithAop")
@Getter
@Setter
public class TransactionWithAopImpl extends AbstractAccountService implements AccountService {

    @Autowired()
    private AccountDao accountDao;

    @Override
    public void transfer(String out, String in, Double money) {
        System.out.println("AOP事务-转账开始");
        System.out.println(
                String.format("用户ID：%s 给用户ID：%s 转账金额：%s，是否模拟异常：%s", out, in, money, needException));
        accountDao.outMoney(out, money);
        if (needException) {
            throw new RuntimeException("AOP事务-转账异常");
        }
        accountDao.inMoney(in, money);
        System.out.println("AOP事务-转账成功！");
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
