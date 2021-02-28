/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: HelloWorldController Author:   xiao Date: 2019/11/13
 * 9:37 PM History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot HelloWorld案例 <br>
 *
 *@RestController和@RequestMapping注解是来自SpringMVC的注解，它们不是SpringBoot的特定部分。<br>
 *
 * 1. @RestController：提供实现了REST API，可以服务JSON,XML或者其他。这里是以String的形式渲染出结果。<br>
 * 2. @RequestMapping：提供路由信息，"/“路径的HTTP Request都会被映射到sayHello方法进行处理。 <br>
 *
 * 具体参考，世界上最好的文档来源自官方的《Spring Framework Document》
 * http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/
 *<br>
 * @RestController注解等价于@Controller+@ResponseBody的结合，使用这个注解的类里面的方法都以json格式输出。
 *<br>
 * 使用命令 mvn spring-boot:run 在命令行启动该应用
 * @projectName spring-boot-learn
 * @title: HelloWorldController
 * @description: TODO
 * @author xiao
 * @create 2019/11/13
 * @since 1.0.0
 */
@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello,World!";
    }
}

