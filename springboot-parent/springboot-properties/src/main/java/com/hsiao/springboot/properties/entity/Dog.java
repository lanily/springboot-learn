/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: Dog Author:   xiao Date:     2019/11/16 9:34 AM
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.properties.entity;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Dog实体<br>
 *
 * @author xiao
 * @Data lombok的注解，会为这个类所有属性添加 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法
 * @Component 加到spring 容器中
 * @ConfigurationProperties 告诉这个类的属性都是配置文件里的属性，prefix指定读取配置文件的前缀
 * @projectName spring-boot-learn
 * @title: Dog
 * @description: TODO
 * @create 2019/11/16
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "person.dog")
public class Dog {

    private String name;
    private Integer age;
}

