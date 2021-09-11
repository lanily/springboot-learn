package com.hsiao.springboot.async;


import com.hsiao.springboot.async.dao.AsyncExecutorTask;
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
 * @title: AsyncExecutorTaskTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncExecutorTaskTest {

    @Autowired
    private AsyncExecutorTask task;

    @Test
    public void testAsyncExecutorTask() throws Exception {
        task.taskOne();
        task.taskTwo();
        task.taskThree();
        Thread.sleep(30 * 1000L);
    }
}

