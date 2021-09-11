package com.hsiao.springboot.retry.exception;


/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: BackendNotAvailableException
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
public class BackendNotAvailableException extends RuntimeException {

    public BackendNotAvailableException(String message) {
        super(message);
    }

    public BackendNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
