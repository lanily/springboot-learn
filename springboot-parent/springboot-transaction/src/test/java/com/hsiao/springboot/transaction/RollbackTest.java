package com.hsiao.springboot.transaction;

import com.hsiao.springboot.transaction.dao.AccountDao;
import com.hsiao.springboot.transaction.service.RollBackService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * spring事务回滚机制<br/>
 * 1、默认回滚机制测试用例：defaultRollback<br/>
 * 2、事务运行时异常和非异常时异常回滚测试用例：rollbackFor1、rollbackFor2、rollbackFor3<br/>
 * 3、事务运行时异常和非运行时异常不回滚测试用例：nonRollback1、nonRollback2<br/>
 *
 * @author hsiao
 * @date 2019/1/24
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class RollbackTest {

    @Autowired
    RollBackService rollBackService;

    @Autowired
    AccountDao accountDao;

    public Double query(String name) {
        return accountDao.query(name);
    }

    @Before
    public void before() {
        System.out.println("===================转账前用户账户余额===================");
        System.out.println("money: " + query("rollback"));
    }


    @After
    public void after() {
        System.out.println("===================转账后用户账户余额===================");
        System.out.println("money: " + query("rollback"));
    }

    @Test
    public void rollback() {

        try {
            rollBackService.rollback();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void noRollback() {
        try {
            rollBackService.noRollback();
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
    }

    @Test
    public void rollbackAuto() {
        try {
            rollBackService.rollbackAuto();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    @Test
    public void rollbackManual() {
        rollBackService.rollbackManual();
    }

    @Test
    public void rollbackPart() {
        rollBackService.rollbackPart();
    }

    @Test
    public void defaultRollback() {
        rollBackService.defaultRollback();
    }

    /**
     * rollback fail since exception is not right
     */
    @Test
    public void rollbackFor1() {
        try {
            rollBackService.rollbackFor1();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void rollbackFor2() {
        try {
            rollBackService.rollbackFor2();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void rollbackFor3() {
        try {
            rollBackService.rollbackFor3();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void nonRollback1() {
        try {
            rollBackService.nonRollback1();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void nonRollback2() {
        try {
            rollBackService.nonRollback2();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
