/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: BookService Author: xiao Date: 2020/4/1 9:34 下午
 * Description: History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.template.thymeleaf.service;

import com.hsiao.springboot.template.thymeleaf.entity.Book;
import java.util.List;

/**
 * Book 业务接口层
 *
 * @author xiao
 * @create 2020/4/1
 * @since 1.0.0
 */
public interface BookService {
  /** 获取所有 Book */
  List<Book> findAll();

  /**
   * 新增 Book
   *
   * @param book {@link Book}
   */
  Book insertByBook(Book book);

  /**
   * 更新 Book
   *
   * @param book {@link Book}
   */
  Book update(Book book);

  /**
   * 删除 Book
   *
   * @param id 编号
   */
  Book delete(Long id);

  /**
   * 获取 Book
   *
   * @param id 编号
   */
  Book findById(Long id);
}
