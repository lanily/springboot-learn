package com.hsiao.springboot.transaction.dao.impl;


import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 *
 * 不生效的demo用例
 *
 * @projectName springboot-parent
 * @title: NotEffectDao
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
@Component
public class NotEffectDao extends JdbcDao {

    @PostConstruct
    public void init() {
        String sql = "replace into account (id, name, money) values"
                + " (520, 'NotEffect', 200),"
                + "(530, 'NotEffect', 200),"
                + "(540, 'NotEffect', 200),"
                + "(550, 'NotEffect', 200)";
        jdbcTemplate.execute(sql);
    }
}

