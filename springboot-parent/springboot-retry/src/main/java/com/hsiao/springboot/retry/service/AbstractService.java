package com.hsiao.springboot.retry.service;


import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: AbstractService
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
//@Service // 这里的@Service可以没有。
public abstract class AbstractService {

    abstract void print();

    /**
     * 这样子在抽象类定义重试，子类的call方法也是可以成功重试的。
     */
    @Retryable(value = {
            RemoteAccessException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000L, multiplier = 1))
    public void call() throws Exception {
        // 子类实现
        print();

        throw new RemoteAccessException("RPC调用异常");
    }
}
