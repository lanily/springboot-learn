package com.hsiao.springboot.retry.service;


import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: RetryTemplateService
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
@Service
@Slf4j
public class RetryTemplateService {

    public void service() throws IllegalAccessException {
        log.info("do something...");
        throw new IllegalAccessException();
    }

    @Autowired
    private RetryTemplate simpleRetryTemplate;

    public <R, T> R retry(Function<T, R> method, T param) throws Throwable {
        return simpleRetryTemplate.execute(new RetryCallback<R, Throwable>() {
            @Override
            public R doWithRetry(RetryContext context) throws Throwable {
                return method.apply(param);
            }
        }, new RecoveryCallback<R>() {
            @Override
            public R recover(RetryContext context) throws Exception {
                return null;
            }
        });
    }
}

