package com.hsiao.springboot.validate.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ServerResponse
 * @description: TODO
 * @author xiao
 * @create 2021/9/11
 * @since 1.0.0
 */
@Data
/** 注解的作用是序列化json时，如果是null对象，key也会消失 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class R<T> implements Serializable {

    /** 状态值 **/
    private int status;
    /** 描述 **/
    private String message;
    /** 数据 **/
    private T data;


    public R(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public R (int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public R(ResultCode resultCode) {
        this.status = resultCode.getCode();
        this.message = resultCode.getDesc();
    }

    public static R success() {
        return new R(ResultCode.SUCCESS);
    }

    public static R success(int status, String message) {
        return new R(status, message);
    }

    public static <T> R success(T data) {
        return success().data(data);
    }

    public static <T> R success(int status, String message, T data) {
        return new R(status, message, data);
    }

    public static R error(String message) {
        return new R(ResultCode.ERROR);
    }

    public static R error(int status, String message) {
        return new R(status, message);
    }

    public static <T> R error(int status, String message, T data) {
        return new R(status, message, data);
    }

    public static R illegalArgument(String msg) {
        return new R(ResultCode.ILLEGAL_ARGUMENT.getCode(), msg);
    }

    public R data(T data) {
        this.setData(data);
        return this;
    }


}

