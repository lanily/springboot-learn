/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: PersonRepository Author:   xiao Date:     2020/10/24
 * 15:21 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.ehcache.repository;


import com.hsiao.springboot.ehcache.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * EmployeeRepository
 *
 * @projectName springboot-parent
 * @title: PersonRepository
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
@Repository
// @Transactional
// @CacheConfig(cacheNames = "employee")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  /** 根据name查询对象 */
  //        @Cacheable(key = "#p0")
  //        Employee findUserByName(String name);
  /** 根据id查询对象 */
  //        @Cacheable(key = "#p0")
  //        Employee findById(Integer id);
  /** 根据id删除 */
  //        @CacheEvict(key = "#p0")
  //        void deleteById(Integer id);
  /** 保存或者更新 当有id时就是更新，否则是添加 */
  //        @Override
  //        @CachePut(key = "#p0.name")
  //        Employee saveAndFlush(Employee user);
  /** 分页查询 */
  //        @Override
  //        @Cacheable(key = "#p0.pageNumber")
  //        Page<Employee> findAll(Pageable pageable);
}
