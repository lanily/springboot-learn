/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: BookRepositoryImpl Author:   xiao Date:
 * 2020/10/24 12:43 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.ehcache.repository;


import com.hsiao.springboot.ehcache.entity.Book;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * @projectName springboot-parent
 * @title: BookRepositoryImpl
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
@Repository
@CacheConfig(cacheNames = "book")
public class BookRepository {

    @Cacheable
    public Book getBookById(Integer id) {
        System.out.println("getBookById");
        Book book = new Book();
        book.setId(id);
        book.setAuthor("罗贯中");
        book.setName("三国演义");
        return book;
    }

    @CachePut(key = "#book.id")
    public Book updateBookById(Book book) {
        System.out.println("uopdateBookById");
        book.setName("三国演义2");
        return book;
    }

    @CacheEvict(key = "#id")
    public void deleteBookById(Integer id) {
        System.out.println("deleteBookById");
    }
}

