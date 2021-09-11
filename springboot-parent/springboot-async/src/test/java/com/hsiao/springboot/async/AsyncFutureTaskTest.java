package com.hsiao.springboot.async;


import com.hsiao.springboot.async.dao.FutureTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
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
 * @title: AsyncFutureTaskTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncFutureTaskTest {

    @Autowired
    private FutureTask futureTask;

    @Test
    public void testFutureTasks() {
        try {
            long start = System.currentTimeMillis();
            Future<String> future1 = futureTask.taskOneCallback();
            Future<String> future2 = futureTask.taskTwoCallBack();
            Future<String> future3 = futureTask.taskThreeCallback();
            //3个任务执行完成之后再执行主程序
            do {
                Thread.sleep(100);
            } while (future1.isDone() && future2.isDone() && future3.isDone());
            log.info("获取异步方法的返回值:{}", future1.get());
            Thread.sleep(5000);
            long end = System.currentTimeMillis();
            log.info("Future主程序执行完成耗时{}秒", (end - start) / 1000f);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

