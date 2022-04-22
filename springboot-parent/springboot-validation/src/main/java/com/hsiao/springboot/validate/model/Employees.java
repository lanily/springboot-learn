package com.hsiao.springboot.validate.model;


import java.util.Map;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: Emplyee
 * @description: TODO
 * @author xiao
 * @create 2021/9/12
 * @since 1.0.0
 */
@Data
public class Employees {

    private Map<@Email(message = "邮箱地址不能为空") String, @NotNull User> users;
}

