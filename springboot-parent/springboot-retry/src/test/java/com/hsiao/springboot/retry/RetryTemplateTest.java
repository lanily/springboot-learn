package com.hsiao.springboot.retry;


import com.hsiao.springboot.retry.service.RetryTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: RetryTemplateTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
@SpringBootTest
@Slf4j
public class RetryTemplateTest {

    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    private RetryTemplateService retryTemplateService;

    @Test
    void test1() throws IllegalAccessException {
        retryTemplate.execute(new RetryCallback<Object, IllegalAccessException>() {
            @Override
            public Object doWithRetry(RetryContext context) throws IllegalAccessException {
                retryTemplateService.service();
                return null;
            }
        });
    }

    @Test
    void test2() throws IllegalAccessException {
        retryTemplate.execute(new RetryCallback<Object, IllegalAccessException>() {
            @Override
            public Object doWithRetry(RetryContext context) throws IllegalAccessException {
                retryTemplateService.service();
                return null;
            }
        }, new RecoveryCallback<Object>() {
            @Override
            public Object recover(RetryContext context) throws Exception {
                log.info("RecoveryCallback....");
                return null;
            }
        });
    }
}

