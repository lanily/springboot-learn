/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: BookContorller Author: xiao Date: 2020/10/24 15:08
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.ehcache.contoller;

import com.hsiao.springboot.ehcache.entity.Book;
import com.hsiao.springboot.ehcache.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @projectName springboot-parent
 * @title: BookContorller
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
@RestController
@RequestMapping("book")
public class BookController {

  @Autowired
  private BookService bookService;

  @GetMapping("get")
  public Book get(@RequestParam int id) {
      return bookService.get(id);
  }

  @PostMapping("set")
  public Book set(@RequestParam int id, @RequestParam String name, @RequestParam String author) {
    Book book = new Book(id, name, author);
    return bookService.set(id, book);
  }

  @DeleteMapping("del")
  public void del(@RequestParam int id) {
    bookService.del(id);
  }
}
