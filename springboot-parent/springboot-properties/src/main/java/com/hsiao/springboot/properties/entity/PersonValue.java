/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: PersonValue Author:   xiao Date:     2019/11/16 9:35
 * AM History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.properties.entity;


import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Email;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author xiao
 * @Data lombok的注解，会为这个类所有属性添加 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法
 * @Component 加到spring 容器中
 * @projectName springboot-learn
 * @title: PersonValue
 * @description: TODO
 * @create 2019/11/16
 * @since 1.0.0
 */
@Data
@Component
@Validated
public class PersonValue {


    /**
     * 直接从配置文件读取一个值
     */
    @Value("${person.last-name}")
    private String lastName;

    /**
     * 支持SpEL表达式
     */
    @Value("#{11*4/2}")
    private Integer age;

    @Value("${person.birth}")
    private Date birth;

    /**
     * 不支持复杂类型
     */
    private Map<String, String> maps;
    private List<String> lists;
    private Dog dog;

    /**
     * 不支持数据校验
     */
    @Email
    @Value("xxx@@@@")
    private String email;
}

