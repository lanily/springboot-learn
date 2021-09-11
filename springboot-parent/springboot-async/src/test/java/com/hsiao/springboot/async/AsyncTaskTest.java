package com.hsiao.springboot.async;


import com.hsiao.springboot.async.dao.AsyncTask;
import com.hsiao.springboot.async.dao.SyncTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: AsyncTaskTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AsyncApplication.class)
public class AsyncTaskTest {

    @Autowired
    private SyncTask syncTask;

    @Autowired
    private AsyncTask asyncTask;


    @Test
    public void testSyncTasks() throws Exception {
        try {
            long start = System.currentTimeMillis();
            syncTask.taskOne();
            syncTask.taskTwo();
            syncTask.taskThree();
            Thread.sleep(5000);
            long end = System.currentTimeMillis();
            log.info("Sync主程序执行完成耗时{}秒", (end - start) / 1000f);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testAsyncTasks() {
        try {
            long start = System.currentTimeMillis();
            asyncTask.taskOne();
            asyncTask.taskTwo();
            asyncTask.taskThree();
            Thread.sleep(5000);
            long end = System.currentTimeMillis();
            log.info("Async主程序执行完成耗时{}秒", (end - start) / 1000f);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}


