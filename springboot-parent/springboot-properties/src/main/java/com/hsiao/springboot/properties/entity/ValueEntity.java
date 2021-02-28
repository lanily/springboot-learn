/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: ValueEntity Author:   xiao Date:     2019/11/16 8:30
 * PM History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.properties.entity;


import java.util.Date;
import java.util.List;
import javax.validation.constraints.Email;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Value 注解语法类 第一步：在属性上添加注解Value注入参数 第二步：把Value注解修饰的类添加到Spring的IOC容器中； 第三步：添加数据校验注解，检查是否支持数据校验；
 *
 * 注意点： 一、nickName和createdDate在yml配置文件中，对应参数分别是中划线和下划线，用于测试其对属性名匹配的松散性 二、email和iphone
 * 测试其支持JSR303数据校验 三、abilities 测试其支持复杂的数据结构
 *
 * 结论： 一、createDate取值必须和yml配置文件中的参数保持一致， 二、既是在iphone上添加邮箱验证注解依然可以通过测试，
 * 三、不支持复杂的数据结构，提示错误和第一条相同：IllegalArgumentException: Could not resolve placeholder 'user.abilities'
 * in value "${user.abilities}"
 *
 * @author xiao
 * @Value详解 第一步：在属性上添加Value注解，通过${}设置参数从配置文件中注入值；
 *
 * 第二步：修改${user.ceatred_date}中的参数值，改为${user.ceatredDate}测试是否能解析成功；
 *
 * 第三步：添加数据校验Validated注解，开启数据校验，测试其是否支持数据校验的功能；
 *
 * 第四步：测试Value注解是否支持SpEL表达式；
 * @projectName spring-boot-learn
 * @title: ValueEntity
 * @description: TODO
 * @create 2019/11/16
 * @since 1.0.0
 */
@Data
@Component
@Validated
public class ValueEntity {

    @Value("${user.nick-name}")
    private String nickName;
    @Value("${user.email}")
    private String email;
    @Email
    @Value("${user.iphone}")        // 解析成功，并不支持数据校验
    private String iphone;
    //    @Value("${user.abilities}")     // 解析错误，并不支持复杂的数据结构
    private List<String> abilities;
    //    @Value("${user.ceatredDate}")   // 解析错误，并不支持松散匹配属性，必须严格一致
    private Date createdDate;

    // Value注解的强大一面：支持SpEL表达式
    @Value("#{(1+2-3)/4*5}")            // 算术运算
    private String operator;
    @Value("#{1>2 || 2 <= 3}")          // 关系运算
    private Boolean comparison;
    @Value("#{systemProperties['java.version']}") // 系统配置：os.name
    private String systemProperties;
    @Value("#{T(java.lang.Math).abs(-18)}") // 表达式
    private String mapExpression;

    // 省略getter，setter，toString方法
}

