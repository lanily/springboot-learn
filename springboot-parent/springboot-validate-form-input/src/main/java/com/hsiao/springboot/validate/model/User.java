/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: User Author: xiao Date: 2020/3/29 7:02 下午 History:
 * <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.validate.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 用户实体类
 *
 * @projectName springboot-parent
 * @title: User
 * @description: TODO
 * @author xiao
 * @create 2020/3/29
 * @since 1.0.0
 */
@Entity
public class User implements Serializable {

  /** 编号 */
  @Id @GeneratedValue private Long id;

  /** 名称 */
  @NotEmpty(message = "姓名不能为空")
  @Size(min = 2, max = 8, message = "姓名长度必须大于 2 且小于 20 字")
  private String name;

  /** 年龄 */
  @NotNull(message = "年龄不能为空")
  @Min(value = 0, message = "年龄大于 0")
  @Max(value = 300, message = "年龄不大于 300")
  private Integer age;

  /** 出生时间 */
  @NotEmpty(message = "出生时间不能为空")
  private String birthday;

  public User(String name, Integer age, String birthday) {
    this.name = name;
    this.age = age;
    this.birthday = birthday;
  }

  public User() {}

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

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  @Override
  public String toString() {
    return "User{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", age="
        + age
        + ", birthday="
        + birthday
        + '}';
  }
}
