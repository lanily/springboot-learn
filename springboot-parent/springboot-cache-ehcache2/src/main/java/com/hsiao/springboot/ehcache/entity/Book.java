/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: Book Author: xiao Date: 2020/10/24 12:27 History:
 * <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.ehcache.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * book entity
 *
 * @projectName springboot-parent
 * @title: Book
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "book")
public class Book implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String name;
  private String author;

  public Book() {}

  @Override
  public String toString() {
    //      return MessageFormat.format("Id:{0}, Name{1}", id, name);
    return "Book{" + "id=" + id + ", name='" + name + '\'' + ", author='" + author + '\'' + '}';
  }
}
