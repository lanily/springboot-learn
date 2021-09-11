package com.hsiao.springboot.retry.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 *
 * RetryTemplate配置
 *
 * 编程式:
 * 先来介绍下编程式使用到的几个主要组件：
 * 重试模板类：org.springframework.retry.support.RetryTemplate
 *
 * 重试策略类：org.springframework.retry.RetryPolicy
 *
 * 重试回退策略（两次重试间等待策略）：org.springframework.retry.backoff.BackOffPolicy
 *
 * 重试上下文：org.springframework.retry.RetryContext
 *
 * 监听器：org.springframework.retry.RetryListener
 *
 * @projectName springboot-parent
 * @title: AppConfig
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
@Configuration
//@EnableRetry(proxyTargetClass = true)
public class AppConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
       // 最大重试次数策略
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(); //设置重试策略
        retryPolicy.setMaxAttempts(2);
        retryTemplate.setRetryPolicy(retryPolicy);

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy(); //设置退避策略
        // 每隔2s后再重试
        fixedBackOffPolicy.setBackOffPeriod(2000L);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        return retryTemplate;
    }
}
