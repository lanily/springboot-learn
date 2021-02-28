/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: PersonRepository Author: xiao Date: 2020/10/24 15:21
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.batch.insert.repository;

import com.hsiao.springboot.batch.insert.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * EmployeeRepository
 *  1. 使用jpa的 CrudRepository 基本查询
 *  2. 使用jpa的 PagingAndSortingRepository 分页查询和排序
 *  3. 使用jpa的 Repository 自定义声明式查询方法
 *
 *     public interface EmployeeRepository extends Repository<Employee, Long> {
 *     // declare query method
 *     // 声明式查询方法
 *     // 1. count 计数
 *     long countByName(String name);
 *     // 2. get/find/stream/query/read 查询
 *     Person readFirstByAge(int age);
 *     // 3. delete/remove 删除
 *      @Transactional
 *      int deleteById(long id);
 *      }
 *
 *  4. 使用jpa的 JpaRepository 使用hql、jpql或sql查询，@Query等注解
 *      public interface EmployeeHqlDao extends JpaRepository<Employee, Long> {
 *          // 使用hql 或者 jpql 查询
 *          @Query("from Person where name = ?1 order by id desc")
 *          List<Person> listByName(String name);
 *          // 前几种方法中均未介绍update操作，要完成update操作，可使用以下方法
 *          // 更新时需要加上 @Transactional 和 @Modifying
 *          @Transactional
 *          @Modifying // QueryExecutionRequestException: Not supported for DML operations
 *          @Query("update Person set name=?2 where id=?1")
 *          int updateNameById(long id, String name);
 *      }
 *
 * @projectName springboot-parent
 * @title: EmployeeRepository
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {}
