package com.hsiao.springboot.transaction;


import com.hsiao.springboot.transaction.service.SimpleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * SpringBoot跑个单元测试只需要在测试类加两个注解就行了。
 *
 * @RunWith(SpringRunner.class)
 * @SpringBootTest
 *
 * 然而这样的单元测试默认是提交事务的，一般的场景下都是要对事务进行回滚的。要支持回滚，只需要增加一个@Transactional注解即可。
 *
 * @RunWith(SpringRunner.class)
 * @SpringBootTest
 * @Transactional
 * 单独的@Transactional是回滚事务，在添加@Transactional的情况下如果要提交事务，只需要增加@Rollback(false)：
 *
 * @RunWith(SpringRunner.class)
 * @SpringBootTest
 * @Transactional
 * @Rollback(false)
 * 由于@Rollback可以用在方法上，所以一个测试类中，我们可以实现部分测试方法用@Rollback回滚事务，部分测试方法用@Rollback(false)来提交事务。
 *
 * 简单注解事务测试用例
 *
 * @projectName springboot-parent
 * @title: SimpleTransactionTest
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleTransactionTest {

    @Autowired
    private SimpleService simpleTransactionService;

    @Test
    public void testNoTransaction() {
        // 不会生效，因为test方法上没有事务注解
        simpleTransactionService.testNoTransaction();
    }

    @Test
    public void testRuntimeExceptionTrans() {
        System.out.println("============ 事务正常工作 start ========== ");
        simpleTransactionService.query("transaction before", 130);
        try {
            // 事务可以正常工作
            simpleTransactionService.testRuntimeExceptionTrans(130);
        } catch (Exception e) {
        }
        simpleTransactionService.query("transaction end", 130);
        System.out.println("============ 事务正常工作 end ========== \n");
    }

    @Test
    public void testNormalException () {
        System.out.println("============ 事务不生效 start ========== ");
        simpleTransactionService.query("transaction before", 140);
        try {
            // 因为抛出的是非运行异常，不会回滚
            boolean isSuccess= simpleTransactionService.testNormalException(140);
            System.out.println("成功=？" + isSuccess);
        } catch (Exception e) {
        }
        simpleTransactionService.query("transaction end", 140);
        System.out.println("============ 事务不生效 end ========== \n");
    }

    @Test
    public void testSpecialException () {
        System.out.println("============ 事务生效 start ========== ");
        simpleTransactionService.query("transaction before", 150);
        try {
            // 注解中，指定所有异常都回滚
            simpleTransactionService.testSpecialException(150);
        } catch (Exception e) {
        }
        simpleTransactionService.query("transaction end", 150);
        System.out.println("============ 事务生效 end ========== \n");
    }

}

