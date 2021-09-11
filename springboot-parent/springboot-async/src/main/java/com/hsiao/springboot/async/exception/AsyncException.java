package com.hsiao.springboot.async.exception;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: AsyncException
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class AsyncException extends Exception {
    private int code;
    private String msg;
}

