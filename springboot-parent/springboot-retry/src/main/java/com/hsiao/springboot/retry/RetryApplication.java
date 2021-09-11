package com.hsiao.springboot.retry;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 *
 * 声明式:
 *
 * 核心注解3个: @EnableRetry,@Retryable 和 @Recover
 *
 * @EnableRetry：此注解用于开启重试框架，可以修饰在SpringBoot启动类上面，也可以修饰在需要重试的类上
 *    proxyTargetClass：Boolean类型，用于指明代理方式【true：cglib代理，false：jdk动态代理】默认使用jdk动态代理
 * @Retryable
 *    value：Class[]类型，用于指定需要重试的异常类型，
 *    include：Class[]类型，作用于value类似，区别尚未分析
 *    exclude：Class[]类型，指定不需要重试的异常类型
 *    maxAttemps：int类型，指定最多重试次数，默认3
 *    backoff：Backoff类型，指明补偿机制
 *    @BackOff
 *       delay:指定延迟后重试，默认为1000L,即1s后开始重试 ;
 *       multiplier:指定延迟的倍数
 * @Recover
 *    当重试次数耗尽依然出现异常时，执行此异常对应的@Recover方法。
 *    异常类型需要与Recover方法参数类型保持一致，
 *    recover方法返回值需要与重试方法返回值保证一致
 *
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
 * 采坑记录:
 *
 * 1. retry重试机制无效
 * 由于retry用到了aspect增强，所有会有aspect的坑，就是方法内部调用，会使aspect增强失效，那么retry当然也会失效。
 *
 * 2. recover回调报错
 * org.springframework.retry.ExhaustedRetryException: Cannot locate recovery method
 * 报错显示找不到recovery方法
 *
 * 解决方案就这这两句话:
 *
 * 异常类型需要与Recover方法参数类型保持一致
 *
 * recover方法返回值需要与重试方法返回值保证一致
 *
 * 异常类型和返回值要一致!
 * @projectName springboot-parent
 * @title: RetryApplication
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
//@EnableRetry   //开启重试机制
@EnableRetry(proxyTargetClass = true) //表示使用cglib代理
//@EnableAutoConfiguration //开启自动配置
@SpringBootApplication
public class RetryApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetryApplication.class, args);
    }
}

