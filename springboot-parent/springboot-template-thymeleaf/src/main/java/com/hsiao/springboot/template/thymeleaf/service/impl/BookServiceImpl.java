/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: BookServiceImpl Author:   xiao Date:     2020/4/1
 * 9:35 下午 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.template.thymeleaf.service.impl;


import com.hsiao.springboot.template.thymeleaf.entity.Book;
import com.hsiao.springboot.template.thymeleaf.service.BookService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * Book 业务层实现
 * @projectName springboot-parent
 * @title: BookServiceImpl
 * @description: TODO
 * @author xiao
 * @create 2020/4/1
 * @since 1.0.0
 */
@Service
public class BookServiceImpl implements BookService {

    // 模拟数据库，存储 Book 信息
    private static Map<Long, Book> BOOK_DB = new HashMap<>();

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(BOOK_DB.values());
    }

    @Override
    public Book insertByBook(Book book) {
        book.setId(BOOK_DB.size() + 1L);
        BOOK_DB.put(book.getId(), book);
        return book;
    }

    @Override
    public Book update(Book book) {
        BOOK_DB.put(book.getId(), book);
        return book;
    }

    @Override
    public Book delete(Long id) {
        return BOOK_DB.remove(id);
    }

    @Override
    public Book findById(Long id) {
        return BOOK_DB.get(id);
    }
}

