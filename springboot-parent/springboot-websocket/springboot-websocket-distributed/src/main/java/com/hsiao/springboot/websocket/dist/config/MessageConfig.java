package com.hsiao.springboot.websocket.dist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "message")
public class MessageConfig {

    /**
     * 支持single、multi、distribute
     */
    private String sessionMode = "single";

    /**
     * 通过redis共享传递的消息池
     * 配置即启用
     */
    private String msgPoolKey;      //"czy:msg_pool:"


    /**
     * 服务根路径
     */
    private String serverRoot;


    public String getSessionMode() {
        return sessionMode;
    }

    public void setSessionMode(String sessionMode) {
        this.sessionMode = sessionMode;
    }

    public String getMsgPoolKey() {
        return msgPoolKey;
    }

    public void setMsgPoolKey(String msgPoolKey) {
        this.msgPoolKey = msgPoolKey;
    }

    public String getServerRoot() {
        return serverRoot;
    }

    public void setServerRoot(String serverRoot) {
        this.serverRoot = serverRoot;
    }
}
