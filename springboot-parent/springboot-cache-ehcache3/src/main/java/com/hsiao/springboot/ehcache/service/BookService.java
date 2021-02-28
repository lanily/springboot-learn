/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: BookService Author:   xiao Date:     2020/11/14 09:10
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.ehcache.service;


import com.hsiao.springboot.ehcache.entity.Book;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: BookService
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
@Component
public class BookService {

    public List<Book> getBooks (Date asOfDate, String title) {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, "The Conterfeiters", "adre gide"));
        books.add(new Book(2, "peer gynt and Hedda Gabler", "henrik ibsen"));
        return books;
    }

}

