package com.hsiao.springboot.async.dao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * 指定前面配置的 线程池的名称taskExecutor
 *
 * @projectName springboot-parent
 * @title: AsyncExecutorTask
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@Component
@Slf4j
public class AsyncExecutorTask extends AbstractTask {

    @Async("taskExecutor")
    public void doTaskOne() throws Exception {
        super.taskOne();
        log.info("任务一，当前线程：" + Thread.currentThread().getName());
    }

    @Async("taskExecutor")
    public void doTaskTwo() throws Exception {
        super.taskTwo();
        log.info("任务二，当前线程：" + Thread.currentThread().getName());
    }

    @Async("taskExecutor")
    public void doTaskThree() throws Exception {
        super.taskThree();
        log.info("任务三，当前线程：" + Thread.currentThread().getName());
    }
}

