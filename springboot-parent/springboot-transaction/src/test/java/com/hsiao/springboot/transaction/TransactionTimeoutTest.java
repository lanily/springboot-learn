package com.hsiao.springboot.transaction;

import com.hsiao.springboot.transaction.service.TxTimeoutService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * spring事务超时机制<br/>
 * @Transactional注解配置了timeout属性和去掉timeout属性事务的超时是否回滚
 *
 * @author hsiao
 * @date 2019/1/24
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionTimeoutTest {


    @Autowired
    private TxTimeoutService txTimeoutService;

    @Test
    public void txTimeoutTest() {
        txTimeoutService.timeoutTransaction();
    }
}
