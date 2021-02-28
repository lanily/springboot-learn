/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: Application Author:   xiao Date:     2019/11/13 10:13
 * PM History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Spring Boot 应用启动类 <br>
 *
 * @author xiao
 * @projectName spring-boot-learn
 * @title: Application
 * @description: TODO
 * @create 2019/11/13
 * @since 1.0.0
 */
@SpringBootApplication
@ImportResource(value = {"classpath:application-bean.xml"})
public class SpringBootConfigurationApplication {

    public static void main(String[] args) {
        // 程序启动入口
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        SpringApplication.run(SpringBootConfigurationApplication.class, args);
    }
}

