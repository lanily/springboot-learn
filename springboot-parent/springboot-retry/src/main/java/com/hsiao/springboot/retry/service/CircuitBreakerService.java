package com.hsiao.springboot.retry.service;


import java.util.concurrent.TimeUnit;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

/**
 *
 * 熔断模式：指在具体的重试机制下失败后打开断路器，过了一段时间，断路器进入半开状态，允许一个进入重试，若失败再次进入断路器，成功则关闭断路器，注解为@CircuitBreaker,具体包括熔断打开时间、重置过期时间
 *
 * @projectName springboot-parent
 * @title: CircuitBreakerService
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
public class CircuitBreakerService {

    // openTimeout时间范围内失败maxAttempts次数后，熔断打开resetTimeout时长
    @CircuitBreaker(maxAttempts = 3, openTimeout = 1000, resetTimeout = 2000, label = "test-CircuitBreaker", include = RuntimeException.class, exclude = Exception.class)
    public void circuitBreaker(int num) throws InterruptedException {
        System.err.print(" 进入断路器方法num=" + num);
        if (num > 8) {
            return;
        }
        Integer n = null;
        System.err.println(1 / n);
    }

    @Retryable(label = "test-Retryable", maxAttempts = 3, backoff = @Backoff(delay = 1), include = RuntimeException.class, exclude = Exception.class)
    public void retryable(int num) throws InterruptedException {
        System.err.println(" 进入重试方法num=" + num);
        if (num > 10) {
            return;
        }
        Integer n = null;
        System.err.println(1 / n);
    }

    @EventListener
    public void contextEvent(ContextRefreshedEvent contextEvent) throws InterruptedException {
        CircuitBreakerService application = contextEvent.getApplicationContext()
                .getBean(CircuitBreakerService.class);
        System.err.println("尝试进入断路器方法，并触发异常");
        application.circuitBreaker(1);

        application.circuitBreaker(1);
        application.circuitBreaker(1);

        System.err.println("尝试进入断路器方法，在openTimeout时间内，触发异常超过3次，断路器打开，断路器方法不允许执行，直接执行恢复方法");
        application.circuitBreaker(2);
        TimeUnit.SECONDS.sleep(2);
        System.err.println("超过断路器半开时间resetTimeout，断路器半开，断路器方法运行允许3个访问进入");
        application.circuitBreaker(3);
        application.circuitBreaker(3);
        application.circuitBreaker(3);
        System.err.println("尝试进入断路器方法，在openTimeout时间内，触发异常超过3次，断路器打开，断路器方法不允许执行，直接执行恢复方法");
        application.circuitBreaker(4);
        TimeUnit.SECONDS.sleep(3);
        System.err.println("超过断路器再次超过半开时间openTimeout+resetTimeout，断路器半开，断路器方法运行允许三个访问进入");
        application.circuitBreaker(5);
        application.circuitBreaker(5);
        application.circuitBreaker(5);
        System.err.println("尝试进入断路器方法，在openTimeout时间内，触发异常超过3次，断路器打开，断路器方法不允许执行，直接执行恢复方法");
        application.circuitBreaker(6);
        TimeUnit.SECONDS.sleep(3);
        System.err.println("超过断路器再次超过半开时间openTimeout+resetTimeout，断路器半开，断路器方法运行允许三个访问进入");
        application.circuitBreaker(7);
        application.circuitBreaker(7);
        application.circuitBreaker(7);
        System.err.println("尝试进入断路器方法，在openTimeout时间内，触发异常超过3次，断路器打开，断路器方法不允许执行，直接执行恢复方法");
        application.circuitBreaker(8);
        TimeUnit.SECONDS.sleep(3);
        System.err.println(
                "超过断路器再次超过半开时间openTimeout+resetTimeout，断路器半开，断路器方法运行允许三个访问进入,并且断路方法不再抛出异常,断路器关闭，方法可持续调用");
        application.circuitBreaker(9);
        application.circuitBreaker(9);
        application.circuitBreaker(9);
        application.circuitBreaker(9);
        application.circuitBreaker(9);
        application.circuitBreaker(9);

        System.err.println();
        System.err.println();
        System.err.println("开始重试");
        application.retryable(10);
        System.err.println("未抛出异常，");
        application.retryable(11);
    }

    @Recover
    public void recover(NullPointerException exception) {
        System.err.println(" NullPointerException ....");
    }

    @Recover
    public void recover(RuntimeException exception) {
        System.err.println(" RuntimeException ....");
    }

    @Recover
    public void recover(Exception exception) {
        System.err.println(" exception ....");
    }

    @Recover
    public void recover(Throwable throwable) {
        System.err.println(" throwable ....");
    }

    @Recover
    public void recover() {
        System.err.println(" recover ....");
    }
}

