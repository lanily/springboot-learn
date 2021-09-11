package com.hsiao.springboot.retry;


import com.hsiao.springboot.retry.exception.RetryException;
import com.hsiao.springboot.retry.service.RetryServices;
import java.util.concurrent.TimeUnit;
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
 * @title: RetryServicesTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RetryServicesTest {

    @Autowired
    private RetryServices retryService;


    @Test
    void testService1() throws IllegalAccessException {
        retryService.service1();
    }

    @Test
    void testService2() throws IllegalAccessException {
        retryService.service2();
    }

    @Test
    void testService3() throws IllegalAccessException {
        retryService.service3();
    }

    @Test
    void testService3_1() throws IllegalAccessException {
        retryService.service3_1();
    }

    @Test
    void testService3_2() throws IllegalAccessException {
        retryService.service3_2();
    }

    @Test
    void testService4() throws IllegalAccessException {
        retryService.service4("hello world");
    }

    @Test
    void testService4_2() throws IllegalAccessException {
        retryService.service4("test message");
    }

    @Test
    void testService4_3() throws IllegalAccessException {
        retryService.service4_3("test message");
    }

    @Test
    void testService5() throws IllegalAccessException {
        retryService.service5("test message");
    }

    @Test
    void testService5_1() throws IllegalAccessException {
        retryService.service5_1("test message");
    }

    @Test
    void testService5_2() {
        retryService.service5_2("test message");
    }

    @Test
    void testService5_3() {
        retryService.service5_3("test message");
    }

    @Test
    void testService5_4() throws RetryException {
        retryService.service5_4("test message");
    }

    @Test
    void testService5_5() throws RetryException {
        retryService.service5_5("test message");
    }

    @Test
    void testService6() throws RetryException {
        retryService.service6("test message");
    }

    @Test
    void testService7() throws IllegalAccessException {
        retryService.service7();
    }

    @Test
    void testService8() throws IllegalAccessException {
        retryService.service8();
    }

    @Test
    void testService9() throws IllegalAccessException {
        retryService.service9();
    }

    @Test
    void testService10() throws IllegalAccessException {
        retryService.service10();
    }

    @Test
    void testService11() throws IllegalAccessException {
        retryService.service11();
    }

    @Test
    void testService12() throws IllegalAccessException {
        retryService.service12();
    }

    @Test
    void testService13() throws IllegalAccessException {
        retryService.service13("test message!!");
    }


    @Test
    public void testCircuitBreaker() throws InterruptedException {
        System.err.println("尝试进入断路器方法，并触发异常...");
        retryService.circuitBreaker(1);
        retryService.circuitBreaker(1);
        retryService.circuitBreaker(9);
        retryService.circuitBreaker(9);
        System.err.println("在openTimeout 1秒之内重试次数为2次，未达到触发熔断, 断路器依然闭合...");
        TimeUnit.SECONDS.sleep(1);
        System.err.println("超过openTimeout 1秒之后, 因为未触发熔断，所以重试次数重置，可以正常访问...,继续重试3次方法...");
        retryService.circuitBreaker(1);
        retryService.circuitBreaker(1);
        retryService.circuitBreaker(1);
        System.err.println("在openTimeout 1秒之内重试次数为3次，达到触发熔断，不会执行重试，只会执行恢复方法...");
        retryService.circuitBreaker(1);
        TimeUnit.SECONDS.sleep(2);
        retryService.circuitBreaker(9);
        TimeUnit.SECONDS.sleep(3);
        System.err.println("超过resetTimeout 3秒之后，断路器重新闭合...,可以正常访问");
        retryService.circuitBreaker(9);
        retryService.circuitBreaker(9);
        retryService.circuitBreaker(9);
        retryService.circuitBreaker(9);
        retryService.circuitBreaker(9);

    }
}

