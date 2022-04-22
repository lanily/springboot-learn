package com.hsiao.springboot.websocket.stomp.model;


import lombok.Data;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: User
 * @description: TODO
 * @author xiao
 * @create 2022/4/21
 * @since 1.0.0
 */
@Data
public class User {
    private String username;
    private String password;
    private String name;
    private String sex;
}
