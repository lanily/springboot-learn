package com.hsiao.springboot.transaction;


import com.hsiao.springboot.transaction.service.AccountService;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: TransactionProxyFactoryBeanTest
 * @description: TODO
 * @author xiao
 * @create 2021/2/27
 * @since 1.0.0
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionProxyFactoryBeanTest {

    @Resource(name = "transactionProxyFactoryBean")
    private AccountService accountService;

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 在测试本方法前，请先修改jdbc.properties配置文件中的数据库，并按给定的sql语句建表
     *
     * 思路：
     *     1. 采用AOP的方式，通过配置org.springframework.transaction.interceptor.TransactionProxyFactoryBean来管理事务和生成代理
     *           注入需要代理的业务、事务管理器、事务的属性
     *     2. 使用生成的代理来执行业务
     */
    @Test
    public void testTransfer() {
        // 现在是可以正常转账状态，在AccountServiceImpl中解开第10行注释会发生异常，可以测试在异常发生时的事务处理结果
        accountService.transfer("aaa", "bbb", 100d);
        System.out.println("转账正常完成");
    }
}

