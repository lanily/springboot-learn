package com.hsiao.springboot.transaction;

import com.hsiao.springboot.transaction.entity.Account;
import com.hsiao.springboot.transaction.service.AccountService;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 此类测试事务配置的5种方式<br/>
 * 1、每个Bean都有一个代理（基于TransactionProxyFactoryBean代理），使用@Qualifier("transactionWithSingleTxProxyBean")<br/>
 * 2、所有Bean共享一个代理基类（基于TransactionProxyFactoryBean代理），使用@Qualifier("transactionWithSharedTxProxyBean")<br/>
 * 3、使用拦截器，使用@Qualifier("withInterceptorService")<br/>
 * 4、使用tx标签配置的拦截器(AOP)，使用@Qualifier("transactionWithAop")<br/>
 * 5、全注解(方法或类加@Transactional)，使用@Qualifier("transactionWithAnnotation")<br/>
 * 另外一种不使用任何事务管理的测试，使用@Qualifier("userBalanceNoTransaction")<br/>
 * 可根据以上配置分别测试正常流程（normalflow测试方式）和异常流程（exceptionflow方法）<br/>
 *
 * @author hemingzhun
 * @date 2019/1/24
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountTransactionTest {

    @Autowired
//    @Qualifier("accountNoTransaction")
//    @Qualifier("transactionWithSingleTxProxyBean")
//    @Qualifier("transactionWithSharedTxProxyBean")
//    @Qualifier("withInterceptorService")
    @Qualifier("transactionWithAop")
//    @Qualifier("transactionWithAnnotation")
    AccountService accountService;

    @Test
    public void normalFlow() {
        System.out.println("===================转账前用户账户信息===================");
        List<Account> beforeList = accountService.getAllList();
        for (Account account : beforeList) {
            System.out.println(account.toString());
        }
//        accountService.transfer("aaa", "bbb", 50d);
        accountService.transfer("bbb", "aaa", 50d);
        List<Account> afterList = accountService.getAllList();
        System.out.println("===================转账后用户账户信息===================");
        for (Account account: afterList) {
            System.out.println(account.toString());
        }
    }

    @Test
    public void exceptionFlow() {
        accountService.setNeedException(true);
        System.out.println("===================转账前用户账户信息===================");
        List<Account> beforeList = accountService.getAllList();
        for (Account account : beforeList) {
            System.out.println(account.toString());
        }
        try {
//            accountService.transfer("aaa", "bbb", 50d);
            accountService.transfer("bbb", "aaa", 50d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Account> afterList = accountService.getAllList();
        System.out.println("===================转账后用户账户信息===================");
        for (Account account : afterList) {
            System.out.println(account.toString());
        }
    }
}
