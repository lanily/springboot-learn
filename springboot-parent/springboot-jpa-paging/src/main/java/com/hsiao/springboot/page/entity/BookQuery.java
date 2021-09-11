package com.hsiao.springboot.page.entity;


import lombok.Data;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: BookQuery
 * @description: TODO
 * @author xiao
 * @create 2021/3/28
 * @since 1.0.0
 */
@Data
public class BookQuery {
    private String name;
    private String isbn;
    private String author;
}

