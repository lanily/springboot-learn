package com.hsiao.springboot.swagger.model;


import com.hsiao.springboot.swagger.constant.ResponseCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * API 返回结果
 *
 * @projectName springboot-parent
 * @title: RestResult
 * @description: API 返回结果
 * @author xiao
 * @create 2021/9/12
 * @since 1.0.0
 */
//@Schema(name="api返回数据模型1",title = "api返回数据模型2")
@ApiModel("api通用返回数据")
public class RestResult<T> {

    //uuid,用作唯一标识符，供序列化和反序列化时检测是否一致
    private static final long serialVersionUID = 7498483649536881777L;
    //标识代码，0表示成功，非0表示出错
    @ApiModelProperty("标识代码,0表示成功，非0表示出错")
    private Integer code;

    //提示信息，通常供报错时使用
    @ApiModelProperty("提示信息,供报错时使用")
    private String msg;

    //正常返回时返回的数据
    @ApiModelProperty("返回的数据")
    private T data;

    public RestResult() {
    }

    //constructor
    public RestResult(Integer status, String msg, T data) {
        this.code = status;
        this.msg = msg;
        this.data = data;
    }

    //返回成功数据
    public RestResult success(T data) {
        return new RestResult(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    public static RestResult success(Integer code, String msg) {
        return new RestResult(code, msg, null);
    }

    //返回出错数据
    public static RestResult error(ResponseCode code) {
        return new RestResult(code.getCode(), code.getMessage(), null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
