/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: MessageConfigurationTest Author:   xiao Date:
 * 2019/11/13 10:17 PM History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.annotation;


import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Spring Boot MessageConfiguration 测试 - {@link MessageConfiguration}<br>
 *
 * @author xiao
 * @projectName spring-boot-learn
 * @title: MessageConfigurationTest
 * @description: TODO
 * @create 2019/11/13
 * @since 1.0.0
 */
public class MessageConfigurationTest {

    @Test
    public void testGetMessageBean() throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
                MessageConfiguration.class);
        assertEquals("message configuration", ctx.getBean("message"));
    }

    @Test
    public void testScanPackages() throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.scan("org.spring.springboot");
        ctx.refresh();
        assertEquals("message configuration", ctx.getBean("message"));
    }
}

