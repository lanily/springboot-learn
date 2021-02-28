/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: Book Author:   xiao Date:     2020/11/14 08:55
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.ehcache.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: Book
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class Book {

    private int id;
    private String title;
    private String author;

}

