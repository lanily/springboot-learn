/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: BatchService Author: xiao Date: 2020/11/14 15:10
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.batch.insert.service;

import com.hsiao.springboot.batch.insert.entity.Employee;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * 〈一句话功能简述〉<br>
 * 批量插入，更新
 *
 * <p>1. service实现层通过@PersistenceContext注解注入EntityManager接口。 2.
 * 批量写入调用persist方法（参数为实体对象），再调用flush刷新到数据库，后clear。 3. 更新数据调用merge（参数为实体对象），同样调用flush刷新到数据库，后clear。
 *
 * @projectName springboot-parent
 * @title: BatchService
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
@Service
@Transactional
@Slf4j
public class BatchService {
  @PersistenceContext private EntityManager entityManager;

  @Autowired private JdbcTemplate jdbcTemplate;

  /**
   * 批量插入
   *
   * @param list 实体类集合
   * @param <T> 表对应的实体类
   */
  public <T> void batchInsert(List<T> list) {
    if (!ObjectUtils.isEmpty(list)) {
      for (int i = 0; i < list.size(); i++) {
        entityManager.persist(list.get(i));
        if (i % 50 == 0) {
          entityManager.flush();
          entityManager.clear();
        }
      }
      entityManager.flush();
      entityManager.clear();
    }
  }

  /**
   * 批量更新
   *
   * @param list 实体类集合
   * @param <T> 表对应的实体类
   */
  public <T> void batchUpdate(List<T> list) {
    if (!ObjectUtils.isEmpty(list)) {
      for (int i = 0; i < list.size(); i++) {
        entityManager.merge(list.get(i));
        if (i % 50 == 0) {
          entityManager.flush();
          entityManager.clear();
        }
      }
      entityManager.flush();
      entityManager.clear();
    }
  }

  public void saveBatch(List<Employee> list) {
    /*String sql="insert into tb_employees " +
            "(firstName, lastName)" +
            " values (?)";
    List<Object[]> batchArgs=new ArrayList<Object[]>();
    for (int i = 0; i < list.size(); i++) {
        batchArgs.add(new Object[]{list.get(i)});
    }
    jdbcTemplate.batchUpdate(sql, batchArgs);*/

    StringBuilder insert =
        new StringBuilder("INSERT INTO `tb_employees` (`first_name`, , `last_name`) VALUES ");
    for (int i = 0; i < list.size(); i++) {
      insert
          .append("(")
          .append("'")
          .append(list.get(i).getFirstName())
          .append("'")
          .append(",")
          .append("'")
          .append(list.get(i).getLastName())
          .append("'")
          .append(")");
      if (i < list.size() - 1) {
        insert.append(",");
      }
    }
    //        String sql = (String) JSONObject.toJSONString(insert);
    //        log.info("SQL语句:{}", JSONObject.toJSONString(insert));
    String sql = insert.toString();
    //        log.info("SQL语句:{}", sql);
    try {
      jdbcTemplate.execute(sql);
    } catch (Exception e) {
      log.error("sql解析错误", e.getMessage());
    }
  }
}
