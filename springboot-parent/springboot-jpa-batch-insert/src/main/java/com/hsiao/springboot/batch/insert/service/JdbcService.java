/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: JdbcService Author:   xiao Date:     2020/11/14 17:03
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.batch.insert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 一、需求背景
 *
 * <p>SpringBoot没有提供封装好的批量插入的方法，所以自己网上了找了些资料关于批量插入的，不太理想，想要实现语句的批量插入，而不是每条每条插入。
 *
 * <p>解决方案 1. 配置JPA的批量插入参数，使用JPA提供的方法saveAll保存，打印SQL发现实际还是单条SQL的插入。
 * spring.jpa.properties.hibernate.jdbc.batch_size=500
 * spring.jpa.properties.hibernate.order_inserts=true
 * spring.jpa.properties.hibernate.order_updates
 * =true
 */
@Service
public class JdbcService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public  void saveMsgItemList(List list) {
        // 这里注意VALUES要用实体的变量，而不是字段的Column值
        String sql = "INSERT INTO t_msg_item(groupid, sender_uid) " +
                "VALUES (:groupId, :senderUid)";
        updateBatchCore(sql, list);
    }

    /**
     * 2. 使用JdbcTemplate的方法构造SQL语句，实现批量插入。
     * 一定要在jdbc url 加&amp;rewriteBatchedStatements=true才能生效
     * @param sql  自定义sql语句，类似于 "INSERT INTO chen_user(name,age) VALUES (:name,:age)"
     * @param list
     * @param
     */
    public  void updateBatchCore(String sql, List list) {
        SqlParameterSource[] beanSources = SqlParameterSourceUtils.createBatch(list.toArray());
        namedParameterJdbcTemplate.batchUpdate(sql, beanSources);
    }

  /**
   * 3. 将插入的数据切割成100一条一次，推荐不超过1000条数据，循环插入。
   * @Autowired private JdbcService jdbcService;
   *
   * <p>
   *     private void multiInsertMsgItem(List uids, String content, boolean hide) {
   *     int size = uids.size(); List data = new ArrayList();
   *     for (int i = 0; i 0) {
   *        jdbcService.saveMsgItemList(data);
   *        }
   *     }
   */
}
