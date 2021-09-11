package com.hsiao.springboot.async.dao;


import java.util.Random;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * 创建任务抽象类 AbstractTask
 *
 * @projectName springboot-parent
 * @title: AbstractTask
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractTask {
    private static Random random = new Random();

    public void taskOne() throws InterruptedException {
        log.info("开始做任务一");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        log.info("完成任务一，耗时： {}秒", (end - start) / 1000f);
    }

    public void taskTwo() throws InterruptedException {
        log.info("开始做任务二");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        log.info("完成任务二，耗时： {}秒", (end - start) / 1000f);
    }

    public void taskThree() throws InterruptedException {
        log.info("开始做任务三");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        log.info("完成任务三，耗时： {}秒", (end - start) / 1000f);
    }
}

