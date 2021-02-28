/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: Application Author:   xiao Date:     2019/11/16 9:25
 * AM History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.properties;


import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 配置类，相当于传统Spring 开发中的 xml-> bean的配置<br>
 *
 * @author xiao
 * @projectName spring-boot-learn
 * @title: Application
 * @description: TODO
 * @create 2019/11/16
 * @since 1.0.0
 */
@SpringBootApplication
public class SpringBootPropertiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootPropertiesApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            // 开始检查spring boot 提供的 beans
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                //System.out.println(beanName);
            }
        };
    }
}

