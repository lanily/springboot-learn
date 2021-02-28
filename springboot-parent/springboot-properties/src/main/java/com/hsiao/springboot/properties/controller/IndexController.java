/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: IndexController Author:   xiao Date:     2019/11/16
 * 9:24 AM History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * IndexController
 *
 * @author xiao
 * @projectName spring-boot-learn
 * @title: IndexController
 * @description: TODO
 * @create 2019/11/16
 * @since 1.0.0
 */
@Slf4j
@RestController
public class IndexController {

    @Value("${bootapp.description}")
    private String description;

    @RequestMapping("/")
    public String index() {
        log.info(description);
        return "Greetings from Spring Boot!";
    }
}

