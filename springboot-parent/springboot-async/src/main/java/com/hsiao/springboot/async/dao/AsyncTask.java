package com.hsiao.springboot.async.dao;


import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * async异步执行演示类
 *
 * @projectName springboot-parent
 * @title: AsyncTask
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@Slf4j
@Component
public class AsyncTask extends AbstractTask {

    private Random random = new Random();

    //@Async所修饰的函数不要定义为static类型，这样异步调用不会生效
    @Async
//    @Async("taskExecutor")
    public void taskOne() throws InterruptedException {
        super.taskOne();
    }

    @Async
    public void taskTwo() throws InterruptedException {
        super.taskTwo();
    }

    @Async
    public void taskThree() throws InterruptedException {
        super.taskThree();
    }
}

