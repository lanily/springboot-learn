package com.hsiao.springboot.websocket.stomp.model;


import java.security.Principal;

/**
 *
 * websocket登录连接对象
 * <用于保存websocket连接过程中需要存储的业务参数>
 *
 * @projectName springboot-parent
 * @title: WebSocketUser
 * @description: TODO
 * @author xiao
 * @create 2022/4/19
 * @since 1.0.0
 */
public class WebSocketUser implements Principal {
    /**
     * 用户身份标识符
     */
    private String name;

    /**
     * 获取用户登录令牌
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    public WebSocketUser(String name) {
        this.name = name;
    }
}
