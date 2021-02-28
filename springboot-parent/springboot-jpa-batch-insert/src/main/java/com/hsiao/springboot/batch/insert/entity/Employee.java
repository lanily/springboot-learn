/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: Employee Author: xiao Date: 2020/10/24 20:40 History:
 * <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.batch.insert.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Employee entity
 *
 * @projectName springboot-parent
 * @title: Employee
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
// @Table(name = "tb_employess",
//        indexes = {
//                @Index(columnList = "id")},
//        uniqueConstraints = {
//                @UniqueConstraint(columnNames = {"id"})
//        }
// )
@Table(name = "tb_employess")
public class Employee implements Serializable {
  private static final long serialVersionUID = 5517244812959569947L;

  @Id
  // 不能使用GenerationType.IDENTITY
  // 因为如果我们的实体使用GenerationType.IDENTITY生成器，Hibernate将默认禁用批量插入/更新。
  //    @GenericGenerator(
  //            name = "sequence",
  //            strategy = "sequence",
  //            parameters = {
  //                    @org.hibernate.annotations.Parameter(
  //                            name = "sequence",
  //                            value = "sequence"
  //                    )
  //
  //            })
  //    @GeneratedValue(generator = "sequence")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeSeqGen")
  @SequenceGenerator(
      name = "employeeSeqGen",
      sequenceName = "employeeSeqGen",
      initialValue = 1,
      allocationSize = 100)
  public Long id;
  //    @GeneratedValue(strategy = GenerationType.AUTO)
  //    private Long id;
  @Column(name = "first_name", nullable = false, unique = true)
  private String firstName;

  @Column(name = "last_name", nullable = true, unique = true)
  private String lastName;

  @Version private Short version;

  public Employee(String firstName, String lastName) {}
}
