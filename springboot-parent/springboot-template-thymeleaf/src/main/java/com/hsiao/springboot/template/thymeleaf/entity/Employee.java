/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: Employee Author: xiao Date: 2020/4/2 9:37 下午 History:
 * <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.template.thymeleaf.entity;

import java.util.Date;
import lombok.Data;

/**
 * 员工实体
 *
 * @projectName springboot-parent
 * @title: Employee
 * @description: TODO
 * @author xiao
 * @create 2020/4/2
 * @since 1.0.0
 */
@Data
public class Employee {

  private Integer id;
  private String name;

  private String email;
  // 1 male, 0 female
  private Integer gender;
  private Department department;
  private Date birthday;

  public Employee(Integer id, String name, String email, Integer gender, Department department) {
    super();
    this.id = id;
    this.name = name;
    this.email = email;
    this.gender = gender;
    this.department = department;
    this.birthday = new Date();
  }

  public Employee() {}

  @Override
  public String toString() {
    return "Employee{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", email='"
        + email
        + '\''
        + ", gender="
        + gender
        + ", department="
        + department
        + ", birthday="
        + birthday
        + '}';
  }
}
