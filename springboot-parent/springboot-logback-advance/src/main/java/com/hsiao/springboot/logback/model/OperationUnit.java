/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: OperationUnit Author: xiao Date: 2020/11/20 21:42
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.logback.model;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: OperationUnit
 * @description: TODO
 * @author xiao
 * @create 2020/11/20
 * @since 1.0.0
 */
public enum OperationUnit {
  /** 被操作的单元 */
  UNKNOWN("unknown"),
  USER("user"),
  EMPLOYEE("employee"),
  Redis("redis");

  private String value;

  OperationUnit(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
