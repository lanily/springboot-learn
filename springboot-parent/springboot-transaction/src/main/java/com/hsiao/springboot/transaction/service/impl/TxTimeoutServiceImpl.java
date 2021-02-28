package com.hsiao.springboot.transaction.service.impl;

import com.hsiao.springboot.transaction.dao.AccountDao;
import com.hsiao.springboot.transaction.entity.Account;
import com.hsiao.springboot.transaction.service.TxTimeoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TxTimeoutServiceImpl implements TxTimeoutService {

    @Autowired()
    private AccountDao accountDao;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class, timeout = 3)
    @Override
    public void timeoutTransaction() {
        accountDao.insert(new Account("timeout1", 1000));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        accountDao.insert(new Account("timeout2", 2000));
    }
}
