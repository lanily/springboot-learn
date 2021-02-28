/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: User Author:   xiao Date:     2020/3/31 10:36 下午
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.template.thymeleaf.entity;


import lombok.Data;

/**
 * 用户实体
 * @projectName springboot-parent
 * @title: User
 * @description: TODO
 * @author xiao
 * @create 2020/3/31
 * @since 1.0.0
 */
@Data
public class User {
    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private long id;
    private String name;
    private int age;
    private String password;
}

