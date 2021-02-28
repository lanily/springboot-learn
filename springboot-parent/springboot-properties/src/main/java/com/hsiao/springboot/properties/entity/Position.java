/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: Position Author:   xiao Date:     2019/11/16 8:18 PM
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.properties.entity;


import lombok.Data;

/**
 * 职位实体类<br>
 *
 * @author xiao
 * @projectName spring-boot-learn
 * @title: Position
 * @description: TODO
 * @create 2019/11/16
 * @since 1.0.0
 */
@Data
public class Position {

    private String name;
    private Double salary;

}

