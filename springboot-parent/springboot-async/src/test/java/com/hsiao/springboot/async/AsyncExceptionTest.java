package com.hsiao.springboot.async;


import com.hsiao.springboot.async.service.AsyncExceptionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: AsyncExceptionTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
//@SpringBootTest(classes=AsyncApplication.class)
public class AsyncExceptionTest {

        @Autowired
        private AsyncExceptionService asyncDemo;

        @Test
        public void contextLoads() throws InterruptedException, ExecutionException {
            asyncDemo.asyncInvokeSimplest();
            asyncDemo.asyncInvokeWithException("test");
            Future<String> future = asyncDemo.asyncInvokeReturnFuture(100);
            System.out.println(future.get());
        }
    }

