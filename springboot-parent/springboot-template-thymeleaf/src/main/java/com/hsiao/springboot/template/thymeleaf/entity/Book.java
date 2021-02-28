/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: Book Author:   xiao Date:     2020/3/31 10:43 下午
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.template.thymeleaf.entity;


import java.io.Serializable;

/**
 *
 * Book 实体类
 * @projectName springboot-parent
 * @title: Book
 * @description: TODO
 * @author xiao
 * @create 2020/3/31
 * @since 1.0.0
 */

public class Book implements Serializable {

    /**
     * 编号
     */
    private Long id;

    /**
     * 书名
     */
    private String name;

    /**
     * 作者
     */
    private String writer;

    /**
     * 简介
     */
    private String introduction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}

