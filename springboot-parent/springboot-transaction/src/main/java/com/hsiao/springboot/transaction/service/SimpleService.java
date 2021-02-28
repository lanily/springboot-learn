package com.hsiao.springboot.transaction.service;


import com.hsiao.springboot.transaction.dao.impl.SimpleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: SimpleTransaction
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
@Service
public class SimpleService {

    @Autowired
    SimpleDao jdbcDao;

    public void query(String tag, int id) {
        jdbcDao.query(tag, id);
    }

    public void testNoTransaction() {
        System.out.println("============ 事务不生效 start ========== ");
        jdbcDao.query("transaction before", 120);
        try {
            testRuntimeExceptionTrans(120);
        } catch (Exception e) {
            //            e.printStackTrace();
        }
        jdbcDao.query("transaction end", 120);
        System.out.println("============ 事务不生效 end ========== \n");
    }

    /**
     * 运行异常导致回滚
     *
     * @return
     */
    @Transactional
    public boolean testRuntimeExceptionTrans(int id) {
        if (jdbcDao.updateName(id)) {
            jdbcDao.query("after updateMoney name", id);
            if (jdbcDao.updateMoney(id)) {
                return true;
            }
        }
        throw new RuntimeException("更新失败，回滚!");
    }

    @Transactional
    public boolean testNormalException(int id) throws Exception {
        if (jdbcDao.updateName(id)) {
            jdbcDao.query("after updateMoney name", id);
            if (jdbcDao.updateMoney(id)) {
                return true;
            }
        }
        throw new Exception("声明异常");
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean testSpecialException(int id) throws Exception {
        if (jdbcDao.updateName(id)) {
            jdbcDao.query("after updateMoney name", id);
            if (jdbcDao.updateMoney(id)) {
                return true;
            }
        }
        throw new IllegalArgumentException("参数异常");
    }
}

