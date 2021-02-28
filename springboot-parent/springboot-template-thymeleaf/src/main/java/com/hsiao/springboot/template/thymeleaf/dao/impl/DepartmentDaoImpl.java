/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: DepartmentDao Author: xiao Date: 2020/4/2 9:40 下午
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.template.thymeleaf.dao.impl;

import com.hsiao.springboot.template.thymeleaf.dao.DepartmentDao;
import com.hsiao.springboot.template.thymeleaf.entity.Department;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @projectName springboot-parent
 * @title: DepartmentDao
 * @description: TODO
 * @author xiao
 * @create 2020/4/2
 * @since 1.0.0
 */
@Repository
public class DepartmentDaoImpl implements DepartmentDao {
  private static Map<Integer, Department> departments = null;

  static {
    departments = new HashMap<Integer, Department>();

    departments.put(101, new Department(101, "D-AA"));
    departments.put(102, new Department(102, "D-BB"));
    departments.put(103, new Department(103, "D-CC"));
    departments.put(104, new Department(104, "D-DD"));
    departments.put(105, new Department(105, "D-EE"));
  }

  private final Logger log = LoggerFactory.getLogger(DepartmentDaoImpl.class);
  @Autowired private JdbcTemplate jdbcTemplate;

  @Override
  public List<Department> getAll() {
    String sql = "select id, name from t_department";
    List<Department> departments =
        jdbcTemplate.query(
            sql,
            new RowMapper<Department>() {

              @Override
              public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
                Department department = new Department();
                department.setId(rs.getInt("id"));
                department.setName(rs.getString("name"));
                return department;
              }
            });
    return departments;
  }

  @Override
  public Collection<Department> getDepartments() {
    return departments.values();
  }

  @Override
  public Department getDepartment(Integer id) {
    return departments.get(id);
  }
}
