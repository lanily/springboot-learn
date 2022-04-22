package com.hsiao.springboot.validate.common;


/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ResponseCode
 * @description: TODO
 * @author xiao
 * @create 2021/9/11
 * @since 1.0.0
 */
public enum ResultCode {

    SUCCESS(0, "成功"),
    ERROR(1, "失败"),
    ILLEGAL_ARGUMENT(2, "参数错误"),
    EMPTY_RESULT(3, "结果为空"),
    NEED_LOGIN(10, "需要登录");

    private final int code;
    private final String desc;

    ResultCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

