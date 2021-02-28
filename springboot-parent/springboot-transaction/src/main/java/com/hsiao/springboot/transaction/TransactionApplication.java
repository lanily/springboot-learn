package com.hsiao.springboot.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ImportResource("classpath:applicationContext.xml")
public class TransactionApplication
{
    public static void main(String[] args){
        SpringApplication app = new SpringApplication(TransactionApplication.class);
        app.run(args);
    }
}
