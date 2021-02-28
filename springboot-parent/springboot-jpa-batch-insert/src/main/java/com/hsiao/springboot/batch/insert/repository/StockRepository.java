/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: StockRepository Author: xiao Date: 2020/11/14 16:04
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.batch.insert.repository;

import com.hsiao.springboot.batch.insert.entity.IndexCalculated;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 〈一句话功能简述〉<br>
 * Repository.java（附加，个人觉得JPA很方便实用）
 * nativeQuery属性设置为true，可以在value里写原生sql即数据库能直接运行的sql，避开JPA的API这样就很灵活，便于sql优化。
 * 就第一点来说再加上SpringDataJPA与SpringBoot的方便整合，SpringDataJPA确实很方便好用，省去了Mybatis的xml配置。
 *
 * @projectName springboot-parent
 * @title: StockRepository
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
@Repository
public interface StockRepository extends JpaRepository<IndexCalculated, String> {

  @Query(
      nativeQuery = true,
      value =
          "SELECT `stock_code` AS `stockCode`,`stock_name` AS `stockName`,`stock_display_name` AS `stockDisplayName` FROM `stock_security`")
  List<Map<String, Object>> stockIndexInfoOfStock();

  @Query(
      nativeQuery = true,
      value =
          "SELECT `index_code` AS `stockCode`,`index_name` AS `stockName`,`index_display_name` AS `stockDisplayName` FROM `index_info` WHERE `index_code` IN (SELECT `index_code` FROM `index_calculated`)")
  List<Map<String, Object>> stockIndexInfoOfIndex();

  @Query(
      nativeQuery = true,
      value = "SELECT `stock_code` FROM `sector_stock` WHERE `sector_code` IN ?1")
  List<Object> stockOfSector(String[] sectorCodeArr);

  @Query(
      nativeQuery = true,
      value =
          "SELECT `name` AS `sectorName`,`stock_code` AS `stockCode` FROM `industry_sector`,`sector_stock` WHERE industry_sector.`code`=sector_stock.`sector_code`")
  List<Map<String, Object>> industryOfStock();

  @Query(
      nativeQuery = true,
      value =
          "SELECT `index_code` AS `code`,`index_name` AS `name`,`index_display_name` AS `displayName` FROM `index_info` WHERE `index_code` IN (SELECT `index_code` FROM `index_calculated`)")
  List<Map<String, Object>> allIndexInfoOfCalculated();

  @Transactional
  @Modifying
  @Query(
      nativeQuery = true,
      value = "INSERT INTO `index_calculated`(`index_code`,`update_time`) VALUES (?1,?2)")
  int updateIndexCalculated(String indexCode, String updateTime);
}
