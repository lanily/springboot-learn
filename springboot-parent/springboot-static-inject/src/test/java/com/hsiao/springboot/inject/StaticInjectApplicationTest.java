package com.hsiao.springboot.inject;


import com.hsiao.springboot.inject.util.InjectStaticWithSetterMethod;
import com.hsiao.springboot.inject.util.InjectStaticWithMemberVar;
import com.hsiao.springboot.inject.util.CacheUtil;
import com.hsiao.springboot.inject.util.InjectStaticWithConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: StaticInjectApplicationTest
 * @description: TODO
 * @author xiao
 * @create 2021/5/30
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
//@WebAppConfiguration
public class StaticInjectApplicationTest {

    @Test
    public void testInjectStaticWithSetterMethod() {
        InjectStaticWithSetterMethod.cache();
    }

    @Test
    public void testInjectStaticWithMemberVar() {
        InjectStaticWithMemberVar.cache();
    }

    @Test
    public void testInjectStaticWithConstructor() {
        InjectStaticWithConstructor.cache();;
    }

    @Test
    public void testCacheUtil() {
        CacheUtil.cache();
    }
}

