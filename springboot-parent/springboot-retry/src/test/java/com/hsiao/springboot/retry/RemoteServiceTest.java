package com.hsiao.springboot.retry;


import com.hsiao.springboot.retry.service.AbstractService;
import com.hsiao.springboot.retry.service.RemoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: RemoteServiceTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RemoteServiceTest {

    /**
     * 不能调用这个的call方法。因为print没实现嘛。
     */
    @Autowired
    AbstractService abstractService;


    /**
     * 与上面连在一起没有autowired会报错：java.lang.NullPointerException
     */
    @Autowired
    RemoteService remoteService;

    @Test
    public void testCallAbstractObject() throws Exception {
        System.out.println("抽象类的调用。");
        remoteService.call();
    }



}

