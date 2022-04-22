package com.hsiao.springboot.validate.model;


import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: Relation
 * @description: TODO
 * @author xiao
 * @create 2021/9/11
 * @since 1.0.0
 */
@Data
public class Relation {
    @NotBlank(message = "父亲的姓名不能为空")
    private String father;

    @NotBlank(message = "母亲的姓名不能为空")
    private String mother;
}

