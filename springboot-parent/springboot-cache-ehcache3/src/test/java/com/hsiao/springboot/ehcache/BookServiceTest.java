/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: BookServiceTest Author: xiao Date: 2020/11/14 09:29
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.ehcache;

import com.hsiao.springboot.ehcache.entity.Book;
import com.hsiao.springboot.ehcache.service.BookService;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTest {

  @Autowired BookService bookService;

  @Test
  public void testGetBooks() {
    Date date = new Date();
    String title = "HZF";
    List<Book> expect = bookService.getBooks(date, title);
    List<Book> actual = bookService.getBooks(date, title);
    Assert.assertEquals(expect, actual);
  }
}
