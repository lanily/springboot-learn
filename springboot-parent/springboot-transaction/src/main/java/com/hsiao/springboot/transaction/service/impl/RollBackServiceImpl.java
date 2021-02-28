package com.hsiao.springboot.transaction.service.impl;

import com.hsiao.springboot.transaction.dao.AccountDao;
import com.hsiao.springboot.transaction.entity.Account;
import com.hsiao.springboot.transaction.service.RollBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Component
public class RollBackServiceImpl implements RollBackService {

    @Autowired
    AccountDao accountDao;

    public void success() {

        accountDao.update(new Account(555,"rollback", 110 + Math.random()));
        System.out.println("================after success update==================");
    }

    public void exception() {
        System.out.println("===================got an exception===================");
        int i = 1 / 0;
    }

    /**
     * 遇到异常回滚事务
     * @return
     */
    @Transactional(rollbackFor = InterruptedException.class)
    @Override
    public void rollback() throws InterruptedException {
        accountDao.update(new Account(555,"rollback", 120));
        throw new InterruptedException();
    }


    /**
     * 遇到异常不回滚事物
     * @return
     */
    @Transactional(noRollbackFor = InterruptedException.class)
    @Override
    public void noRollback() throws InterruptedException {
        accountDao.update(new Account(555,"rollback", 130));
        throw new InterruptedException();
    }


    /**
     * 自动回滚
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void rollbackAuto() throws Exception {
        success();
        // 假如exception这个操作数据库的方法会抛出异常,方法success()对数据库的操作会回滚
        exception();
    }


    /**
     * 手动回滚（进行try/catch，回滚并抛出）
     */
    @Transactional(rollbackFor = Exception.class)
    public void rollbackManual() {
        success();
        try {
            exception();
        } catch (Exception e) {
            e.printStackTrace();
            //手工回滚异常
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    /**
     * 回滚部分异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void rollbackPart() {
        success();
        //设置回滚点,只回滚以下异常
        Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try {
            exception();
        } catch (Exception e) {
            e.printStackTrace();
            //手工回滚异常,回滚到savePoint
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    @Override
    public void defaultRollback() {
        accountDao.update(new Account(555,"rollback", 140));
        int[] num = new int[3];
        System.out.println(num[4]);
        //throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = ClassNotFoundException.class)
    @Override
    public void rollbackFor1() throws InterruptedException {
        accountDao.update(new Account(555,"rollback", 150));
        throw new InterruptedException();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = InterruptedException.class)
    @Override
    public void rollbackFor2() throws InterruptedException {
        accountDao.update(new Account(555,"rollback", 160));
        throw new InterruptedException();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackForClassName = "java.lang.IOException")
    @Override
    public void rollbackFor3() throws InterruptedException {
        accountDao.update(new Account(555,"rollback", 170));
        throw new InterruptedException();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, noRollbackFor = InterruptedException.class)
    @Override
    public void nonRollback1() throws InterruptedException {
        accountDao.update(new Account(555,"rollback", 180));
        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, noRollbackForClassName = "java.io.RuntimeException")
    @Override
    public void nonRollback2() throws InterruptedException {
        accountDao.update(new Account(555,"rollback", 190));
        throw new InterruptedException();
    }

}
