package com.hsiao.springboot.page.service;

import com.hsiao.springboot.page.entity.Book;
import com.hsiao.springboot.page.entity.BookQuery;
import org.springframework.data.domain.Page;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: BookService
 * @description: TODO
 * @author xiao
 * @create 2021/3/28
 * @since 1.0.0
 */
public interface BookService {

    Page<Book> findBookNoCriteria(Integer page, Integer size);

    Page<Book> findBookCriteria(Integer page, Integer size, BookQuery bookQuery);
}

