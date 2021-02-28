/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: RandomEntity Author:   xiao Date:     2019/11/16 8:34
 * PM History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.properties.entity;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 随机数和占位符语法类  <br>
 *
 * @author xiao
 * @projectName spring-boot-learn
 * @title: RandomEntity
 * @description: TODO
 * @create 2019/11/16
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "ran")
public class RandomEntity {

    private String ranValue;    // 随机生成一个字符串
    private Integer ranInt;     // 随机生成一个整数
    private Long ranLong;       // 随机生成一个长整数
    private Integer ranIntNum;  // 在指定范围内随机生成一个整数
    private Integer ranIntRange;// 在指定区间内随机生成一个整数
    private String ranPlaceholder;// 占位符

    // 省略getter，setter，toString方法e
}

