package com.hsiao.springboot.async.dao;


import java.util.Random;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

/**
 *
 * Future异步执行回调
 *
 * Future获取异步执行结果
 * 需要获取任务的返回值，且在多任务都执行完成后再结束主任务，这个时候又该怎么处理呢？
 *
 * 在多线程里通过Callable和Future可以获取返回值，这里也是类似的，我们使用Future返回方法的执行结果，AsyncResult是Future的一个实现类。
 *
 * 在AsyncResult中：
 *
 * isDone()方法可以用于判断异步方法是否执行完成，若任务完成，则返回true
 * get()方法可用于获取任务执行后返回的结果
 * cancel(boolean mayInterruptIfRunning)可用于取消任务，参数mayInterruptIfRunning表示是否允许取消正在执行却没有执行完毕的任务，如果设置true，则表示可以取消正在执行过程中的任务
 * isCancelled()方法表示任务是否被取消成功，如果在任务正常完成前被取消成功，则返回 true
 * get(long timeout, TimeUnit unit)用来获取执行结果，如果在指定时间内，还没获取到结果，就直接返回null
 *
 * @projectName springboot-parent
 * @title: FutureTask
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@Slf4j
@Component
public class FutureTask  extends AbstractTask {

    private Random random = new Random();

    //@Async所修饰的函数不要定义为static类型，这样异步调用不会生效
    @Async
    public Future<String> taskOneCallback() throws InterruptedException {
        super.taskOne();
        return new AsyncResult<>("任务一Ok");
    }

    @Async
    public Future<String> taskTwoCallBack() throws InterruptedException {
        super.taskTwo();
        return new AsyncResult<>("任务二OK");
    }

    @Async
    public Future<String> taskThreeCallback() throws InterruptedException {
        super.taskThree();
        return new AsyncResult<>("任务三Ok");
    }
}

