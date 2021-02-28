package com.hsiao.springboot.transaction.dao.impl;


import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: PropagationDao
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
@Component
public class PropagationDao extends JdbcDao {

    @PostConstruct
    public void init() {
        String sql = "replace into account (id, name, money) values "
                + "(420, 'Propagation', 200),"
                + "(430, 'Propagation', 200),"
                + "(440, 'Propagation', 200),"
                + "(450, 'Propagation', 200),"
                + "(460, 'Propagation', 200),"
                + "(470, 'Propagation', 200),"
                + "(480, 'Propagation', 200),"
                + "(490, 'Propagation', 200)";
        jdbcTemplate.execute(sql);
    }

    public boolean updateName(int id, String name) {
        String sql = "update money set `name`='" + name + "' where id=" + id;
        jdbcTemplate.execute(sql);
        return true;
    }
}

