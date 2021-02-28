/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: Department Author: xiao Date: 2020/4/2 9:38 下午
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.template.thymeleaf.entity;

import lombok.Data;

/**
 * 部门实体
 *
 * @projectName springboot-parent
 * @title: Department
 * @description: TODO
 * @author xiao
 * @create 2020/4/2
 * @since 1.0.0
 */
@Data
public class Department {

  private Integer id;
  private String name;

  public Department() {}

  public Department(int id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  public String toString() {
    return "Department [id=" + id + ", name=" + name + "]";
  }
}
