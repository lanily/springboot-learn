package com.hsiao.springboot.async.service;


import com.hsiao.springboot.async.dao.SyncTask;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: SyncTaskService
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@Service
@Slf4j
public class SyncTaskService {

    @Autowired
    SyncTask syncTask;

    public void syncTask1() throws InterruptedException {
        syncTask.taskOne();
       log.info(new Date() + ": syncTask1 complete.");
    }

    public void syncTask2() throws InterruptedException {
        Thread.sleep(2000);//模拟阻塞操作
        log.info(new Date() + ": syncTask2 complete.");
    }

    public void syncTask3() throws InterruptedException {
        Thread.sleep(2000);//模拟阻塞操作
        log.info(new Date() + ": syncTask3 complete.");
    }
}

