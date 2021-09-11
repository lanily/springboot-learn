package com.hsiao.springboot.async.dao;


import java.util.concurrent.Future;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: OtherTask
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
public class OtherTask {

    @Async
    public void asyncMethod(String s) {
        System.out.println("receive:" + s);
    }

    public void test() {
        System.out.println("test");
//        asyncMethod();//同一个类里面调用异步方法
    }
    @Async
    public void test2() {
//        AsyncService asyncService  = context.getBean(AsyncService.class);
//        asyncService.asyncMethod();//异步
    }
    /**
     * 异布调用返回Future
     */
    @Async
    public Future<String> asyncInvokeReturnFuture(int i) {
        System.out.println("asyncInvokeReturnFuture, parameter=" + i);
        Future<String> future;
        try {
            Thread.sleep(1000 * 1);
            future = new AsyncResult<String>("success:" + i);
        } catch (InterruptedException e) {
            future = new AsyncResult<String>("error");
        }
        return future;
    }
}

