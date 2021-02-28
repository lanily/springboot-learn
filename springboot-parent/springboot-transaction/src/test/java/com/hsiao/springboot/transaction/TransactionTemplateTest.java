package com.hsiao.springboot.transaction;


import com.hsiao.springboot.transaction.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ApplicationTest
 * @description: TODO
 * @author xiao
 * @create 2021/2/27
 * @since 1.0.0
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionTemplateTest {

    @Autowired
    @Qualifier("accountTransactionTemplate")
    private AccountService accountService;

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 在测试本方法前，请先修改jdbc.properties配置文件中的数据库，并按给定的sql语句建表
     *
     * 思路：
     *     1. DAO层通过JdbcTemplate来操作数据库，简化代码
     *     2. 使用org.springframework.transaction.support.TransactionTemplate管理事务
     *           并向TransactionTemplate中注入事务管理器
     *     3. 在Service层中直接调用TransactionTemplate的execute方法来执行业务
     */
    @Test
    public void testTransfer() {
        // 现在是可以正常转账状态，在AccountServiceImpl中解开第23行注释会发生异常，可以测试在异常发生时的事务处理结果
        accountService.transfer("aaa", "bbb", 100d);
        System.out.println("转账正常完成");
    }
}

