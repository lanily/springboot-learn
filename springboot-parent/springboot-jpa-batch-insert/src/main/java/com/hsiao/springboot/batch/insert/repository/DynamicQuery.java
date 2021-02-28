/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: DynamicQuery Author: xiao Date: 2020/11/14 20:10
 * Description: History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.batch.insert.repository;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 扩展SpringDataJpa, 支持动态jpql/nativesql查询并支持分页查询 使用方法：注入ServiceImpl
 *
 * <p>https://gitee.com/52itstyle/spring-data-jpa/blob/master/src/main/java/com/itstyle/jpa/dynamicquery/
 *
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
public interface DynamicQuery {

  public void save(Object entity);

  public void update(Object entity);

  public <T> void delete(Class<T> entityClass, Object entityid);

  public <T> void delete(Class<T> entityClass, Object[] entityids);

  /**
   * 查询对象列表，返回List
   *
   * @param
   * @param nativeSql
   * @param params
   * @return List<T>
   */
  <T> List<T> nativeQueryList(String nativeSql, Object... params);

  /**
   * 查询对象列表，返回List<Map<key,value>>
   *
   * @param nativeSql
   * @param params
   * @return List<T>
   */
  <T> List<T> nativeQueryListMap(String nativeSql, Object... params);

  /**
   * 查询对象列表，返回List<组合对象>
   *
   * @param resultClass
   * @param nativeSql
   * @param params
   * @return List<T>
   */
  <T> List<T> nativeQueryListModel(Class<T> resultClass, String nativeSql, Object... params);
}
