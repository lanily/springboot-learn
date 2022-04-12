package com.hsiao.springboot.websocket.chat.model;


import lombok.Data;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: User
 * @description: TODO
 * @author xiao
 * @create 2022/4/25
 * @since 1.0.0
 */
@Data
public class User {
    private Integer id;
    private String name;

    public User() {}

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
