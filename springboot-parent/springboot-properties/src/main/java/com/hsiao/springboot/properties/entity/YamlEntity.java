/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: YamlEntity Author:   xiao Date:     2019/11/16 8:11
 * PM History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.properties.entity;


import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * YAML 语法实体类 <br> 切记点： 一、冒号后面加空格，即 key:(空格)value 二、每行参数左边空格数量决定了该参数的层级，不可乱输入。
 *
 *
 * 一、YAML简介 yml是YAML（YAML Ain't Markup Language）语言的文件，以数据为中心，比json、xml等更适合做配置文件
 *
 * yml和xml相比，少了一些结构化的代码，使数据更直接，一目了然。
 *
 * yml和json呢？没有谁好谁坏，合适才是最好的。yml的语法比json优雅，注释更标准，适合做配置文件。json作为一种机器交换格式比yml强，更适合做api调用的数据交换。
 *
 * 一）YAML语法 以空格的缩进程度来控制层级关系。空格的个数并不重要，只要左边空格对齐则视为同一个层级。注意不能用tab代替空格。且大小写敏感。支持字面值，对象，数组三种数据结构，也支持复合结构。
 *
 * 字面值：字符串，布尔类型，数值，日期。字符串默认不加引号，单引号会转义特殊字符。日期格式支持yyyy/MM/dd HH:mm:ss
 *
 * 对象：由键值对组成，形如 key:(空格)value 的数据组成。冒号后面的空格是必须要有的，每组键值对占用一行，且缩进的程度要一致，也可以使用行内写法：{k1: v1, ....kn:
 * vn}
 *
 * 数组：由形如 -(空格)value 的数据组成。短横线后面的空格是必须要有的，每组数据占用一行，且缩进的程度要一致，也可以使用行内写法： [1,2,...n]
 *
 * 复合结构：上面三种数据结构任意组合
 *
 * 二）YAML的运用 创建一个Spring Boot 的全局配置文件 application.yml，配置属性参数。主要有字符串，带特殊字符的字符串，布尔类型，数值，集合，行内集合，行内对象，集合对象这几种常用的数据格式。
 *
 * 三）YML小结 一、字符串可以不加引号，若加双引号则输出特殊字符，若不加或加单引号则转义特殊字符；
 *
 * 二、数组类型，短横线后面要有空格；对象类型，冒号后面要有空格；
 *
 * 三、YAML是以空格缩进的程度来控制层级关系，但不能用tab键代替空格，大小写敏感；
 *
 * 四、如何让一个程序员崩溃？在yml文件中加几个空格！(〃＞皿＜)
 *
 * @author xiao
 * @projectName spring-boot-learn
 * @title: YamlEntity
 * @description: TODO
 * @create 2019/11/16
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "yaml")
public class YamlEntity {

    // 字面值，字符串，布尔，数值
    private String str; // 普通字符串
    private String specialStr; // 转义特殊字符串
    private String specialStr2;// 输出特殊字符串
    private Boolean flag;   // 布尔类型
    private Integer num;    // 整数
    private Double dNum;    // 小数

    // 数组，List和Set，两种写法： 第一种：-空格value，每个值占一行，需缩进对齐；第二种：[1,2,...n] 行内写法
    private List<Object> list;  // list可重复集合
    private Set<Object> set;    // set不可重复集合

    // Map和实体类，两种写法：第一种：key空格value，每个值占一行，需缩进对齐；第二种：{key: value,....} 行内写法
    private Map<String, Object> map; // Map K-V
    private List<Position> positions;  // 复合结构，集合对象

    // 省略getter，setter，toString方法
}

