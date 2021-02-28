/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: DepartmentDao Author: xiao Date: 2020/4/2 10:41 下午
 * Description: History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.template.thymeleaf.dao;

import com.hsiao.springboot.template.thymeleaf.entity.Department;
import java.util.Collection;
import java.util.List;

/**
 * 部门数据操作
 *
 * @author xiao
 * @create 2020/4/2
 * @since 1.0.0
 */
public interface DepartmentDao {
  Collection<Department> getDepartments();

  Department getDepartment(Integer id);

  List<Department> getAll();
}
