package com.hsiao.springboot.transaction.dao.impl;


import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: JdbcDao
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
@Component
public class JdbcDao {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public boolean updateName(int id) {
        String sql = "update account set `name`='更新' where id=" + id;
        jdbcTemplate.execute(sql);
        return true;
    }

    public void query(String tag, int id) {
        String sql = "select id, name, money from account where id=" + id;
        Map map = jdbcTemplate.queryForMap(sql);
        System.out.println(tag + " >>>> " + map);
    }

    public boolean updateMoney(int id) {
        String sql = "update money set `money`= `money` + 10 where id=" + id;
        jdbcTemplate.execute(sql);
        return false;
    }
}

