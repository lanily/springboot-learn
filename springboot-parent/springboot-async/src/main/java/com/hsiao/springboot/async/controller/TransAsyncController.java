package com.hsiao.springboot.async.controller;


import com.hsiao.springboot.async.service.AsyncCallTransService;
import com.hsiao.springboot.async.service.TransCallAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: TransAsyncController
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@RestController
public class TransAsyncController {

    @Autowired
    private AsyncCallTransService asyncCallTransService;

    @Autowired
    private TransCallAsyncService transCallAsyncService;

    /**
     * 异步方法调用事务方法
     * 事务抛异常
     *  async call transaction function will  roll back
     * @return
     */
    @GetMapping("/callTrans")
    public String asyncCallTrans() {
        long startTimeStamp = System.currentTimeMillis();
        asyncCallTransService.callTransTask();
        long endTimeStamp = System.currentTimeMillis();
        String message = "callTrans tasks are triggered successfully, duration: " + (endTimeStamp - startTimeStamp) + " ms";
        System.out.println(message);
        return message;
    }

    /**
     * 事务方法调用异步方法
     * 事务抛异常
     * 也就是说事务在主线程，异步在子线程
     * 主线称的事务异常不影响子线程
     * transaction function call async will not roll back
     * @return
     */
    @GetMapping("/callAsync")
    public String transCallAsync() {
        long startTimeStamp = System.currentTimeMillis();
        transCallAsyncService.callAsyncTask();
        long endTimeStamp = System.currentTimeMillis();
        String message = "callAsync tasks are triggered successfully, duration: " + (endTimeStamp - startTimeStamp) + " ms";
        System.out.println(message);
        return message;

    }
}

