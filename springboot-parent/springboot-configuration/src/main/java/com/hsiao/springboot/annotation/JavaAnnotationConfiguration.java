/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: MessageConfiguration Author:   xiao Date:
 * 2019/11/13 10:15 PM History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.annotation;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * bean 配置<br>
 *
 * @projectName spring-boot-learn
 * @title: MessageConfiguration
 * @description: TODO
 * @author xiao
 * @create 2019/11/13
 * @since 1.0.0
 */
@Configuration
public class JavaAnnotationConfiguration {

    @Bean
    public String message() {
        return "java annotation configuration";
    }
}

