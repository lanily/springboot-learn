/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: HelloWorldControllerTest Author:   xiao Date:
 * 2019/11/13 9:42 PM History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.controller;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Spring Boot HelloWorldController 测试 - {@link HelloWorldController} <br>
 *
 * @author xiao
 * @projectName spring-boot-learn
 * @title: HelloWorldControllerTest
 * @description: TODO
 * @create 2019/11/13
 * @since 1.0.0
 */
public class HelloWorldControllerTest {

    @Test
    public void testHello() {
        assertEquals("Hello,World!", new HelloWorldController().hello());
    }

}

