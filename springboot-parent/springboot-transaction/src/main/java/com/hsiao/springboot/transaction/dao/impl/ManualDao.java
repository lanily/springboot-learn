package com.hsiao.springboot.transaction.dao.impl;


import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ManualDao
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
@Component
public class ManualDao extends JdbcDao {

    @PostConstruct
    public void init() {
        String sql = "replace into account (id, name, money) values "
                + "(220, 'Manual', 200),"
                + "(230, 'Manual', 200),"
                + "(240, 'Manual', 200),"
                + "(250, 'Manual', 200)";
        jdbcTemplate.execute(sql);
    }
}

