/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: TestRedis Author:   xiao Date:     2020/11/19 20:48
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.maven.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: TestRedis
 * @description: TODO
 * @author xiao
 * @create 2020/11/19
 * @since 1.0.0
 */
@PropertySource(value = {"classpath:jdbc.properties"})
@Component
@ConfigurationProperties(prefix = "jdbc")
public class TestJdbc {

    @Value("driver.class")
    private String driveClass;
    @Value("url")
    private String url;
    @Value("username")
    private String username;
    @Value("password")
    private String password;

    @Override
    public String toString() {
        return "TestJDBC{" +
                "driveClass='" + driveClass + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

