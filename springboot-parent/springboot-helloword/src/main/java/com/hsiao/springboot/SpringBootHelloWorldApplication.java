/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: Application Author:   xiao Date:     2019/11/13 9:36
 * PM History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot应用启动类 <br>
 *
 * 1. @SpringBootApplication：Spring Boot 应用的标识<br> 2. Application很简单，一个main函数作为主入口。<br>
 *
 * SpringApplication引导应用，并将Application本身作为参数传递给run方法。<br> 具体run方法会启动嵌入式的Tomcat并初始化Spring环境及其各Spring组件。
 *
 * Spring Boot的基础结构共三个文件（具体路径根据用户生成项目时填写的Group所有差异）：<br>
 *
 * src/main/java下的程序入口：Application<br> src/main/resources下的配置文件：application.properties<br>
 * src/test/下的测试入口：ApplicationTests
 *
 * @author xiao
 * @projectName spring-boot-learn
 * @title: Application
 * @description: TODO
 * @create 2019/11/13
 * @since 1.0.0
 */
@SpringBootApplication
public class SpringBootHelloWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootHelloWorldApplication.class, args);
    }
}

