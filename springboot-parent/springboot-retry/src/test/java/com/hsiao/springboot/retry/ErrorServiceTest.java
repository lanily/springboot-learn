package com.hsiao.springboot.retry;


import com.hsiao.springboot.retry.service.ErrorService;
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
 * @title: ErrorServiceTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ErrorServiceTest {

    /**
     * 如果这样子注入了，就可以重试了。
     */
    @Autowired
    ErrorService notInjectService;

    @Test
    public void testCallByNewObject() throws Exception {
        System.out.println("如果通过new对象来调用，而不是注入，是不会重试的。");
        // notInjectService = new NotInjectService();// 这样子不会触发重试，因为不是注入
        notInjectService.call();
    }
}

