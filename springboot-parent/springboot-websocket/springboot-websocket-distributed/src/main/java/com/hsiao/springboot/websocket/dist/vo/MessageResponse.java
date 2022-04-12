package com.hsiao.springboot.websocket.dist.vo;

import java.io.Serializable;
import java.util.HashMap;

public class MessageResponse extends HashMap implements Serializable {

    private String code;
    private String message;


    public MessageResponse() {
        this("200", "success");
    }

    public MessageResponse(String code, String message) {
        super();
        this.code = code;
        this.message = message;
        this.put("code", code);
        this.put("message", message);
    }

    public static MessageResponse success() {
        return new MessageResponse();
    }

    public static MessageResponse failure(String msg) {
        return new MessageResponse("500", msg);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
