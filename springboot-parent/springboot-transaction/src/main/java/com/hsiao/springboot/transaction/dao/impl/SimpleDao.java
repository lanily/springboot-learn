package com.hsiao.springboot.transaction.dao.impl;


import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: SimpleDao
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
@Component
public class SimpleDao extends JdbcDao{

    @PostConstruct
    public void init() {
        String sql = "replace into account (id, name, money) values " +
                "(120, 'Simple', 200)," +
                "(130, 'Simple', 200)," +
                "(140, 'Simple', 200)," +
                "(150, 'Simple', 200)";
        jdbcTemplate.execute(sql);
    }
}

