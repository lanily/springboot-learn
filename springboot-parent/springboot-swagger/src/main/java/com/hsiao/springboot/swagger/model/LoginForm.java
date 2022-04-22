package com.hsiao.springboot.swagger.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: LoginForm
 * @description: TODO
 * @author xiao
 * @create 2021/9/12
 * @since 1.0.0
 */
// 先使用@ApiModel来标注类
@ApiModel(value = "用户登录表单对象", description = "用户登录表单对象")
public class LoginForm {

    // 使用ApiModelProperty来标注字段属性。
    @ApiModelProperty(value = "用户名", required = true, example = "root")
    private String username;
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;

    // 此处省略入参赋值时需要的getter,setter


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
