package com.hsiao.springboot.transaction.dao.impl;


import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: DetailDao
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
@Component
public class IsolationDao extends JdbcDao {

    @PostConstruct
    public void init() {
        String sql = "replace into account (id, name, money) values "
                + "(320, '初始化', 200),"
                + "(330, '初始化', 200),"
                + "(340, '初始化', 200),"
                + "(350, '初始化', 200)";
        jdbcTemplate.execute(sql);
    }
}

