package com.hsiao.springboot.transaction.service.impl;

import com.hsiao.springboot.transaction.dao.AccountDao;
import com.hsiao.springboot.transaction.entity.Account;
import com.hsiao.springboot.transaction.service.AbstractAccountService;
import com.hsiao.springboot.transaction.service.ReadOnlyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ReadOnlyServiceImpl extends AbstractAccountService implements ReadOnlyService {

    @Autowired()
    private AccountDao accountDao;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public void transfer(String out, String in, Double money) {
        accountDao.outMoney(out, money);
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
}
