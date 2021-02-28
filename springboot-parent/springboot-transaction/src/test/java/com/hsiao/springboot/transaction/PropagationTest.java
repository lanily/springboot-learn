package com.hsiao.springboot.transaction;

import com.hsiao.springboot.transaction.entity.Account;
import com.hsiao.springboot.transaction.service.PropagationAccountService;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * spring事务隔离级别测试<br/>
 * 1、Propagation.REQUIRED传播行为测试用例：propagationrequiredTest<br/>
 * 2、Propagation.REQUIRES_NEW传播行为测试用例：propagationrequires_newTest<br/>
 * 3、Propagation.SUPPORTS传播行为测试用例：propagationsupportsTest<br/>
 * 4、Propagation.NOT_SUPPORTED传播行为测试用例：propagationnot_supportedTest<br/>
 * 5、Propagation.MANDATORY传播行为测试用例：propagationmandatoryTest<br/>
 * 6、Propagation.NESTED传播行为测试用例：propagationnestedTest<br/>
 * 7、Propagation.NEVER传播行为测试用例：propagationneverTest<br/>
 *
 * 多事务嵌套问题解答
 * 1. 同一个类内方法调用：
 * 假定类A的方法a()调用方法b()
 * 同一类内方法调用，无论被调用的b()方法是否配置了事务，此事务在被调用时都将不生效。
 * 1、在同一个类中，一个无事务方法调用另一个有事务注解方法(比如@Async,@Transational)的方法，注解事务是不会生效的
 * 2、在同一个类中，一个有事务方法调用另一个有事务注解方法(比如@Async,@Transational)的方法，只有外层事务有效，被调的注解事务是不会生效的
 *
 * 2. 不同类之间的方法调用：
 * 如类A的方法a()调用类B的方法b()，这种情况事务是正常起作用的。只要方法a()或b()配置了事务，运行中就会开启事务，产生代理。
 * 若两个方法都配置了事务，两个事务具体以何种方式传播，取决于设置的事务传播特性。
 *
 * 3、在不同类中，一个无事务方法调用另一个有事务注解方法(比如@Async,@Transational)的方法，有注解事务是生效的
 * 4、在不同类中，一个有事务方法调用另一个有事务注解方法(比如@Async,@Transational)的方法，两个方法都有事务
 *
 * 原因：
 *
 * spring 在扫描bean的时候会扫描方法上是否包含@Transactional注解，如果包含，spring会为这个bean动态地生成一个子类（即代理类，proxy），代理类是继承原来那个bean的。此时，当这个有注解的方法被调用的时候，实际上是由代理类来调用的，代理类在调用之前就会启动transaction。然而，如果这个有注解的方法是被同一个类中的其他方法调用的，那么该方法的调用并没有通过代理类，而是直接通过原来的那个bean，所以就不会启动transaction，我们看到的现象就是@Transactional注解无效。
 *
 * @author hsiao
 * @date 2019/1/24
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class PropagationTest {

    @Autowired
    PropagationAccountService propagationAccountService;

    @Before
    public void beforQuery() {
        System.out.println("beforQuery获取用户账户信息列表start=========");
        List<Account> beforeList = propagationAccountService.getAllList();
        for (Account account : beforeList) {
            System.out.println(account);
        }
        System.out.println("beforQuery获取用户账户信息列表end=========");
    }

    @After
    public void afterQuery() {
        System.out.println("afterQuery获取用户账户信息列表start=========");
        List<Account> beforeList = propagationAccountService.getAllList();
        for (Account account : beforeList) {
            System.out.println(account);
        }
        System.out.println("afterQuery获取用户账户信息列表end=========");
    }


    @Test
    public void propagationrequiredTest() {
        propagationAccountService.propagationrequiredTest();
    }

    @Test
    public void propagationrequires_newTest() {
        propagationAccountService.propagationrequires_newTest();
    }

    @Test
    public void propagationsupportsTest() {
        propagationAccountService.propagationsupportsTest();
    }

    @Test
    public void propagationnot_supportedTest() {
        propagationAccountService.propagationnot_supportedTest();
    }

    @Test
    public void propagationmandatoryTest() {
        propagationAccountService.propagationmandatoryTest();
    }

    @Test
    public void propagationnestedTest() {
        propagationAccountService.propagationnestedTest();
    }

    @Test
    public void propagationneverTest() {
        propagationAccountService.propagationneverTest();
    }
}
