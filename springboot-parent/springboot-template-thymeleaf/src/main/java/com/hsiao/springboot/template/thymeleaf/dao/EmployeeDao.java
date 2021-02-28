/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: EmployeeDao Author: xiao Date: 2020/4/2 10:43 下午
 * Description: History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.template.thymeleaf.dao;

import com.hsiao.springboot.template.thymeleaf.entity.Employee;
import java.util.Collection;
import java.util.List;

/**
 * @author xiao
 * @create 2020/4/2
 * @since 1.0.0
 */
public interface EmployeeDao {
  void save(Employee employee);

  void delete(Integer id);

  Employee get(Integer id);

  Collection<Employee> getEmployees();

  List<Employee> getAll();
}
