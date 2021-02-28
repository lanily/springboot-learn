package com.hsiao.springboot.transaction.service;


import com.hsiao.springboot.transaction.dao.impl.ManualDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 *
 * 编程式事务
 *
 * @projectName springboot-parent
 * @title: ManualTransactionService
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
@Service
public class ManualService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    ManualDao jdbcDao;

    public void testTransaction(int id) {
        transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus transactionStatus) {
                try {
                    return doUpdate(id);
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    return false;
                }
            }
        });
    }

    public void query(String tag, int id) {
        jdbcDao.query(tag, id);
    }

    private boolean doUpdate(int id) throws Exception {
        if (jdbcDao.updateName(id)) {
            jdbcDao.query("after updateMoney name", id);
            if (jdbcDao.updateMoney(id)) {
                return true;
            }
        }
        throw new Exception("参数异常");
    }
}

