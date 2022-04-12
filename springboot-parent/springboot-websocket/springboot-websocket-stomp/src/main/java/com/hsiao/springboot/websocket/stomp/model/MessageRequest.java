package com.hsiao.springboot.websocket.stomp.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: MessageRequest
 * @description: TODO
 * @author xiao
 * @create 2022/4/19
 * @since 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageRequest implements Serializable {

    /**
     * 消息编码
     */
    private String code;
    /**
     * 来自（保证唯一）
     * 消息发送者，对应认证用户Principal.name （全局唯一）
     */
    private String from;
    /**
     * 到哪（保证唯一）
     * 消息接收者，对应认证用户Principal.name （全局唯一）
     */
    private String to;
    private String content;

}
