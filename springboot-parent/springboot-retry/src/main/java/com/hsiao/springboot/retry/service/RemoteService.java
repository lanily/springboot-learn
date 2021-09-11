package com.hsiao.springboot.retry.service;


import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * 抽象类有@service不管用，因为注入的是Child Service，因此没有@service会报错：NoSuchBeanDefinitionException:
 * No qualifying bean of type 'com.danni.everytry.AbstractService' available:
 * expected at least 1 bean which qualifies as autowire candidate. Dependency
 * annotations:
 * {@org.springframework.beans.factory.annotation.Autowired(required=true)}
 *
 * @projectName springboot-parent
 * @title: RemoteService
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
@Service
public class RemoteService extends AbstractService {

    @Override
    void print() {
        System.out.println("Remote Service print.");
    }

    /**
     * 如果把父类的call复写了，就不会重试了。除非里面也调用super.call()
     */
    @Override
    @Retryable(value = {
            RemoteAccessException.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000l, multiplier = 1))
    public void call() throws Exception {
        System.out.println("Remote Service call start.");

        // 如果不调用调用super.call()，不会重试，除非这个方法也@Retryalbe
        super.call();

        // 这一句是不会打印的，因为上面call里抛了异常
        System.out.println("Remote Service call finished.");
        throw new RemoteAccessException("RPC调用异常");
    }

    @Recover
    public void recover(RemoteAccessException e) {
        System.out.println(e.getMessage());
    }

}

