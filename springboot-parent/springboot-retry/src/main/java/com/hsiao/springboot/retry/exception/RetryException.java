package com.hsiao.springboot.retry.exception;


import lombok.Getter;
import lombok.Setter;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: RetryException
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
@Getter
@Setter
public class RetryException extends Exception {

    private String myMessage;

    public RetryException(String myMessage) {
        this.myMessage = myMessage;
    }
}

