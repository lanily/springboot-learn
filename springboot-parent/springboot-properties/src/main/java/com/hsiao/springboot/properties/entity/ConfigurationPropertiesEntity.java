/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: ConfigurationPropertiesEntity Author:   xiao Date:
 * 2019/11/16 8:28 PM History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.properties.entity;

import java.util.Date;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * ConfigurationProperties 注解语法类<br> 第一步：导入依赖 spring-boot-configuration-processor；
 * 第二步：把ConfigurationProperties注解修饰的类添加到Spring的IOC容器中； 第三步：设置prefix属性，指定需要注入属性的前缀；
 * 第四步：添加数据校验注解，开启数据校验；
 *
 * 注意点： 一、nickName和createdDate在yml配置文件中，对应参数分别是中划线和下划线，用于测试其对属性名匹配的松散性 二、email和iphone
 * 测试其支持JSR303数据校验 三、abilities 测试其支持复杂的数据结构
 *
 * 四）配置文件取值小结 一、ConfigurationProperties注解支持批量注入，而Value注解适合单个注入；
 *
 * 二、ConfigurationProperties注解支持数据校验，而Value注解不支持；
 *
 * 三、ConfigurationProperties注解支持松散匹配属性，而Value注解必须严格匹配属性；
 *
 * 四、ConfigurationProperties不支持强大的SpEL表达式，而Value支持；
 *
 * @author xiao
 * @projectName spring-boot-learn
 * @title: ConfigurationPropertiesEntity
 * @description: TODO
 * @create 2019/11/16
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "user")
@Validated
public class ConfigurationPropertiesEntity {

    private String nickName;    // 解析成功，支持松散匹配属性
    private String email;
    //    @Email                      // 解析失败，数据校验成功：BindValidationException: Binding validation errors on user
    private String iphone;
    private List<String> abilities;
    private Date createdDate;   // 解析成功，支持松散匹配属性

    //    @ConfigurationProperties("#{(1+2-3)/4*5}")
    private String operator;    // 语法报错，不支持SpEL表达式：not applicable to field

    // 省略getter，setter，toString方法
}

