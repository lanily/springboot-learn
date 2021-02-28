/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: TestController Author: xiao Date: 2020/11/19 20:40
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.maven.controller;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: TestController
 * @description: TODO
 * @author xiao
 * @create 2020/11/19
 * @since 1.0.0
 */
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PropertySource("classpath:application.yml")
// 读取application.yml文件
public class TestController {

  // 获取项目端口号
  @Value("${server.port}")
  private String servePrort;

  @Value("${server.servlet.context-path}")
  private String contextPath;

  @Value("${redis}")
  private String redis;

  // http://localhost:8088/SpringBoot/get
  @GetMapping("/get")
  public void get() {
    // 获取项目端口号server.port=8088
    System.out.println("项目端口号为：" + servePrort);
    // 获取获取项目名称
    System.out.println("获取项目名称为：" + contextPath);
    // 获取自定义属性redis
    System.out.println("获取项目名称为：" + redis);
  }
}
