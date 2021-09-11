package com.hsiao.springboot.retry;


import com.hsiao.springboot.retry.service.RetryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: RetryServiceTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RetryServiceTest {

    @Autowired
    private RetryService retryService;


    @Test
    public void retryTest() {
        retryService.retry(-1);
    }
}

