package com.hsiao.springboot.async.controller;


import com.hsiao.springboot.async.service.SyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: SyncTaskController
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@RestController
public class SyncTaskController {


    @Autowired
    private SyncTaskService syncTaskService;

    @GetMapping("/sync")
    @ResponseBody
    public String execute() throws InterruptedException {
        long startTimeStamp = System.currentTimeMillis();
        syncTaskService.syncTask1();
        syncTaskService.syncTask2();
        syncTaskService.syncTask3();
        long endTimeStamp = System.currentTimeMillis();
        String message =
                "sync tasks are complete, duration: " + (endTimeStamp - startTimeStamp) + " ms";
        System.out.println(message);
        return message;
    }
}

