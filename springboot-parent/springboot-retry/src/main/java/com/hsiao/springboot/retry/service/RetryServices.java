package com.hsiao.springboot.retry.service;


import com.hsiao.springboot.retry.exception.RetryException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: RetryExpressionService
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
@Service
@Slf4j
public class RetryServices {

    @Retryable(value = IllegalAccessException.class)
    public void service1() throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        throw new IllegalAccessException("manual exception");
    }


    @Retryable(include = IllegalAccessException.class, maxAttempts = 5)
    public void service2() throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        throw new IllegalAccessException("manual exception");
    }

    @Retryable(value = IllegalAccessException.class, maxAttemptsExpression = "${maxAttempts}")
    public void service3() throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        throw new IllegalAccessException("manual exception");
    }

    @Retryable(value = IllegalAccessException.class, maxAttemptsExpression = "#{1+1}")
    public void service3_1() throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        throw new IllegalAccessException("manual exception");
    }

    @Retryable(value = IllegalAccessException.class, maxAttemptsExpression = "#{${maxAttempts}}")
    public void service3_2() throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        throw new IllegalAccessException("manual exception");
    }


    @Retryable(value = IllegalAccessException.class, exceptionExpression = "message.contains('test')")
    public void service4(String exceptionMessage) throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        throw new IllegalAccessException(exceptionMessage);
    }

    @Retryable(value = IllegalAccessException.class, exceptionExpression = "#{message.contains('test')}")
    public void service4_3(String exceptionMessage) throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        throw new IllegalAccessException(exceptionMessage);
    }

    @Retryable(value = IllegalAccessException.class, exceptionExpression = "#{@retryService.checkException(#root)}")
    public void service5(String exceptionMessage) throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        throw new IllegalAccessException(exceptionMessage);
    }

    @Retryable(value = IllegalAccessException.class, exceptionExpression = "@retryService.checkException(#root)")
    public void service5_1(String exceptionMessage) throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        throw new IllegalAccessException(exceptionMessage);
    }

    @Retryable(exceptionExpression = "#{#root instanceof T(java.lang.IllegalAccessException)}")
    //判断exception的类型
    public void service5_2(String exceptionMessage) {
        log.info("do something... {}", LocalDateTime.now());
        throw new NullPointerException(exceptionMessage);
    }

    @Retryable(exceptionExpression = "#root instanceof T(java.lang.IllegalAccessException)")
    public void service5_3(String exceptionMessage) {
        log.info("do something... {}", LocalDateTime.now());
        throw new NullPointerException(exceptionMessage);
    }

    @Retryable(exceptionExpression = "myMessage.contains('test')")
    //查看自定义的MyException中的myMessage的值是否包含test字符串
    public void service5_4(String exceptionMessage) throws RetryException {
        log.info("do something... {}", LocalDateTime.now());
        throw new RetryException(exceptionMessage);
    }

    @Retryable(exceptionExpression = "#root.myMessage.contains('test')") //和上面service5_4方法的效果一样
    public void service5_5(String exceptionMessage) throws RetryException {
        log.info("do something... {}", LocalDateTime.now());
        throw new RetryException(exceptionMessage);
    }


    public boolean checkException(Exception e) {
        log.error("error message:{}", e.getMessage());
        return true;//返回true的话表明会执行重试，如果返回false则不会执行重试
    }


    @Retryable(exclude = RetryException.class)
    public void service6(String exceptionMessage) throws RetryException {
        log.info("do something... {}", LocalDateTime.now());
        throw new RetryException(exceptionMessage);
    }


    @Retryable(value = IllegalAccessException.class,
            backoff = @Backoff(value = 2000))
    public void service7() throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        throw new IllegalAccessException();
    }


    @Retryable(value = IllegalAccessException.class,
            backoff = @Backoff(value = 2000, delay = 500))
    public void service8() throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        throw new IllegalAccessException();
    }

    @Retryable(value = IllegalAccessException.class, maxAttempts = 4,
            backoff = @Backoff(delay = 2000, multiplier = 2))
    public void service9() throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        throw new IllegalAccessException();
    }

    @Retryable(value = IllegalAccessException.class, maxAttempts = 4,
            backoff = @Backoff(delay = 2000, multiplier = 2, maxDelay = 5000))
    public void service10() throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        throw new IllegalAccessException();
    }


    @Retryable(value = IllegalAccessException.class)
    public void service11() throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        throw new IllegalAccessException();
    }


    @Recover
    public void recover11(IllegalAccessException e) {
        log.info("service retry after Recover => {}", e.getMessage());
    }

    //=========================

    @Retryable(value = ArithmeticException.class)
    public int service12() throws IllegalAccessException {
        log.info("do something... {}", LocalDateTime.now());
        return 1 / 0;
    }


    @Recover
    public int recover12(ArithmeticException e) {
        log.info("service retry after Recover => {}", e.getMessage());
        return 0;
    }

    //=========================

    @Retryable(value = ArithmeticException.class)
    public int service13(String message) throws IllegalAccessException {
        log.info("do something... {},{}", message, LocalDateTime.now());
        return 1 / 0;
    }


    @Recover
    public int recover13(ArithmeticException e, String message) {
        log.info("{},service retry after Recover => {}", message, e.getMessage());
        return 0;
    }


    // openTimeout时间范围内失败maxAttempts次数后，熔断打开resetTimeout时长
    @CircuitBreaker(openTimeout = 1000, resetTimeout = 3000, value = NullPointerException.class)
    public void circuitBreaker(int num) {
        log.info(" 进入断路器方法num={}", num);
        if (num > 8) {
            return;
        }
        Integer n = null;
        System.err.println(1 / n);
    }


    @Recover
    public void recover(NullPointerException e) {
        log.info("service retry after Recover => {}", e.getMessage());
    }

}

