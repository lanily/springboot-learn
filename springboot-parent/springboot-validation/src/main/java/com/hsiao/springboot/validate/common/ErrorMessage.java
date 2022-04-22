package com.hsiao.springboot.validate.common;


import lombok.Data;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ErrorMessage
 * @description: TODO
 * @author xiao
 * @create 2021/9/11
 * @since 1.0.0
 */
@Data
public class ErrorMessage {

    private String propertyPath;

    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(String propertyPath, String message) {
        this.propertyPath = propertyPath;
        this.message = message;
    }
}
