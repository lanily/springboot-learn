/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: User Author:   xiao Date:     2019/11/16 8:21 PM
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.properties.entity;


import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用户实体类<br>
 *
 * 用户信息
 *
 * @author xiao
 * @ConfigurationProperties : 被修饰类中的所有属性会和配置文件中的指定值（该值通过prefix找到）进行绑定 二、Properties简介
 * properties文件大家经常用，这里就简单介绍一下。其语法结构形如：key=value。注意中文乱码问题，需要转码成ASCII。具体如下所示： 三、配置文件取值 Spring
 * Boot通过ConfigurationProperties注解从配置文件中获取属性。从上面的例子可以看出ConfigurationProperties注解可以通过设置prefix指定需要批量导入的数据。支持获取字面值，集合，Map，对象等复杂数据。ConfigurationProperties注解还有其他特么呢？它和Spring的Value注解又有什么区别呢？带着这些问题，我们继续往下看。(๑•̀ㅂ•́)و✧
 *
 * 一）ConfigurationProperties和Value优缺点 ConfigurationProperties注解的优缺点
 *
 * 一、可以从配置文件中批量注入属性；
 *
 * 二、支持获取复杂的数据类型；
 *
 * 三、对属性名匹配的要求较低，比如user-name，user_name，userName，USER_NAME都可以取值；
 *
 * 四、支持JAVA的JSR303数据校验；
 *
 * 五、缺点是不支持强大的SpEL表达式；
 *
 * Value注解的优缺点正好相反，它只能一个个配置注入值；不支持数组、集合等复杂的数据类型；不支持数据校验；对属性名匹配有严格的要求。最大的特点是支持SpEL表达式，使其拥有更丰富的功能。
 *
 * 二）@ConfigurationProperties详解 第一步：导入依赖。若要使用ConfigurationProperties注解，需要导入依赖
 * spring-boot-configuration-processor；
 *
 * 第二步：配置数据。在application.yml配置文件中，配置属性参数，其前缀为user，参数有字面值和数组，用来判断是否支持获取复杂属性的能力；
 *
 * 第三步：匹配数据。在类上添加注解ConfigurationProperties，并设置prefix属性值为user。并把该类添加到Spring的IOC容器中。
 *
 * 第四步：校验数据。添加数据校验Validated注解，开启数据校验，测试其是否支持数据校验的功能；
 *
 * 第五步：测试ConfigurationProperties注解是否支持SpEL表达式；
 *
 * 导入依赖：pom.xml 添加 spring-boot-configuration-processor依赖
 * @projectName spring-boot-learn
 * @title: User
 * @description: TODO
 * @create 2019/11/16
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "user")
public class User {

    private String account;
    private Integer age;
    private Boolean active;
    private Date createdDate;
    private Map<String, Object> map;
    private List<Object> list;
    private Position position;

    // 省略getter，setter，toString方法

}

