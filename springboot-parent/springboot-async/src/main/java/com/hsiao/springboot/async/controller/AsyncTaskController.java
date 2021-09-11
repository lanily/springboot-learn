package com.hsiao.springboot.async.controller;


import com.hsiao.springboot.async.dao.AsyncTask;
import com.hsiao.springboot.async.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: AsyncTaskController
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */

@RestController
public class AsyncTaskController {

    @Autowired
    private AsyncTaskService asyncTaskService;

    @GetMapping("/async")
    @ResponseBody
    public String execute() throws InterruptedException {
        long startTimeStamp = System.currentTimeMillis();
        asyncTaskService.asyncTask1();
        asyncTaskService.asyncTask2();
        asyncTaskService.asyncTask3();
        long endTimeStamp = System.currentTimeMillis();
        String message = "async tasks are triggered successfully, duration: " + (endTimeStamp - startTimeStamp) + " ms";
        System.out.println(message);
        return message;
    }

    @Autowired
    private AsyncTask asyncTask;
//    @RequestMapping("/future")
//    public String doTask() throws InterruptedException {
//        long currentTimeMillis = System.currentTimeMillis();
//        Future<String> task1 = asyncTask.task1();
//        Future<String> task2 = asyncTask.task2();
//        Future<String> task3 = asyncTask.task3();
//        String result = null;
//        for (; ; ) {
//            if (task1.isDone() && task2.isDone() && task3.isDone()) {
//               // 三个任务都调用完成，退出循环等待
//                break;
//           }
//            Thread.sleep(1000);
//       }
//        long currentTimeMillis1 = System.currentTimeMillis();
//        result = "task任务总耗时:" + (currentTimeMillis1 - currentTimeMillis) + "ms";
//       return result;
//    }
//
}