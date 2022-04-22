package com.hsiao.springboot.websocket.stomp.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: MessgaeResponse
 * @description: TODO
 * @author xiao
 * @create 2022/4/20
 * @since 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageResponse implements Serializable {

    public MessageResponse(String content) {
        this.content = content;
    }

    private String content;
    private Date date;
}
