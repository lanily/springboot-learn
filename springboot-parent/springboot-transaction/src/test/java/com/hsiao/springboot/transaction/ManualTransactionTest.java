package com.hsiao.springboot.transaction;


import com.hsiao.springboot.transaction.service.ManualService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * 编程式事务测试用例
 *
 * @projectName springboot-parent
 * @title: ManualTransactionTest
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ManualTransactionTest {

    @Autowired
    private ManualService manualTransactionService;

    @Test
    public void testManualCase() {
        System.out.println("======= 编程式事务 start ========== ");
        manualTransactionService.query("transaction before", 220);
        manualTransactionService.testTransaction(220);
        manualTransactionService.query("transaction end", 220);
        System.out.println("======= 编程式事务 end ========== ");
    }
}

