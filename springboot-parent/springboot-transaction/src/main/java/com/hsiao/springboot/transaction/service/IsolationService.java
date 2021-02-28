package com.hsiao.springboot.transaction.service;


import com.hsiao.springboot.transaction.dao.impl.IsolationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * 事务隔离级别测试
 *
 * @projectName springboot-parent
 * @title: DetailTransactionService
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
@Service
public class IsolationService {

    @Autowired
    IsolationDao jdbcDao;

    // ---------- rr 事物隔离级别

    /**
     * 只读事务，主要目的是为了隔离其他事务的修改，对本次操作的影响；
     *
     * 比如在某些耗时的涉及多次表的读取操作中，为了保证数据一致性，这个就有用了； 开启只读事务之后，不支持修改数据
     */
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public boolean readRrTransaction(int id) throws InterruptedException {
        jdbcDao.query("rr read only", id);
        Thread.sleep(3000);
        jdbcDao.query("rr read only", id);
        return true;
    }

    /**
     * rr隔离级别事务，读事务禁止其他的写事务，未提交写事务，会挂起其他读写事务；可避免脏读，不可重复读，（我个人认为，innodb引擎可通过mvvc+gap锁避免幻读）
     *
     * @param id
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public boolean rrTransaction(int id) {
        if (jdbcDao.updateName(id)) {
            jdbcDao.query("rr: after updateMoney name", id);
            if (jdbcDao.updateMoney(id)) {
                return true;
            }
        }

        return false;
    }

    // ---------- ru 事物隔离级别
    // 测试脏读，会读取到另外一个事务未提交的修改

    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED, rollbackFor = Exception.class)
    public boolean readRuTransaction(int id) throws InterruptedException {
        jdbcDao.query("ru read only", id);
        Thread.sleep(1000);
        jdbcDao.query("ru read only", id);
        return true;
    }

    /**
     * ru隔离级别的事务，可能出现脏读，不可避免不可重复读，幻读
     *
     * @param id
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = Exception.class)
    public boolean ruTransaction(int id) throws InterruptedException {
        if (jdbcDao.updateName(id)) {
            jdbcDao.query("ru: after updateMoney name", id);
            Thread.sleep(2000);
            if (jdbcDao.updateMoney(id)) {
                return true;
            }
        }
        jdbcDao.query("ru: after updateMoney money", id);
        return false;
    }

    // ---------- rc 事物隔离级别
    // 测试不可重复读，一个事务内，两次读取的结果不一样


    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean readRcTransaction(int id) throws InterruptedException {
        jdbcDao.query("rc read only", id);
        Thread.sleep(1000);
        jdbcDao.query("rc read only", id);
        Thread.sleep(3000);
        jdbcDao.query("rc read only", id);
        return true;
    }

    /**
     * rc隔离级别事务，未提交的写事务，会挂起其他的读写事务；可避免脏读，更新丢失；但不能防止不可重复读、幻读
     *
     * @param id
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean rcTranaction(int id) throws InterruptedException {
        if (jdbcDao.updateName(id)) {
            jdbcDao.query("rc: after updateMoney name", id);
            Thread.sleep(2000);
            if (jdbcDao.updateMoney(id)) {
                return true;
            }
        }

        return false;
    }

    // ---------- serialize 事物隔离级别
    // 事务之间串行执行


    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public boolean readSerializeTransaction(int id) throws InterruptedException {
        jdbcDao.query("serialize read only", id);
        Thread.sleep(3000);
        jdbcDao.query("serialize read only", id);
        return true;
    }

    /**
     * serialize，事务串行执行，fix所有问题，但是性能低
     *
     * @param id
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public boolean serializeTransaction(int id) {
        if (jdbcDao.updateName(id)) {
            jdbcDao.query("serialize: after updateMoney name", id);
            if (jdbcDao.updateMoney(id)) {
                return true;
            }
        }

        return false;
    }

    public void query(String tag, int id) {
        jdbcDao.query(tag, id);
    }

}

