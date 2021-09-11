package com.hsiao.springboot.async.service;


import com.hsiao.springboot.async.dao.AsyncTask;
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
public class AsyncTaskService {

    @Autowired
    AsyncTask asyncTask;

    public void asyncTask1() throws InterruptedException {
        asyncTask.taskOne();
        log.info(new Date() + ": asyncTask1 complete.");
    }

    public void asyncTask2() throws InterruptedException {
        asyncTask.taskTwo();

        log.info(new Date() + ": asyncTask2 complete.");
    }

    public void asyncTask3() throws InterruptedException {
        asyncTask.taskThree();

        log.info(new Date() + ": asyncTask3 complete.");
    }
}

