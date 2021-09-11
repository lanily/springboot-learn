package com.hsiao.springboot.transaction.dao.impl;


import com.hsiao.springboot.transaction.dao.AccountDao;
import com.hsiao.springboot.transaction.entity.Account;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: AccountDaoImpl
 * @description: TODO
 * @author xiao
 * @create 2021/2/27
 * @since 1.0.0
 */
public class  AccountDaoImpl extends JdbcDaoSupport implements AccountDao {

    @Override
    public void outMoney(String out, Double money) {
        String sql = "update account set money = money - ? where name = ?";
        this.getJdbcTemplate().update(sql, money, out);

    }

    @Override
    public void inMoney(String in, Double money) {
        String sql = "update account set money = money + ? where name = ?";
        this.getJdbcTemplate().update(sql, money, in);

    }

    @Override
    public Double query(String name) {
        return this.getJdbcTemplate().queryForObject(
                "select money from account where name = ?",
                Double.class,
                name
        );
    }

    @Override
    public void insert(Account account) {
        this.getJdbcTemplate()
                .update("insert into account (name,money) values(?,?)", account.getName(),
                        account.getMoney());
    }

    @Override
    public void delete(int id) {
        this.getJdbcTemplate().update("delete from account where id=?", id);
    }

    @Override
    public void update(Account account) {
        this.getJdbcTemplate()
                .update("update account set name=?,balance=? where id=?", account.getName(),
                        account.getMoney(), account.getId());
    }

    @Override
    public List<Account> getAllList() {
        return this.getJdbcTemplate()
                .query("select id,name,money from account", new RowMapper<Account>() {
                    @Override
                    public Account mapRow(ResultSet resultSet, int i) throws SQLException {
                        Account account = new Account();
                        account.setId(resultSet.getInt("id"));
                        account.setName(resultSet.getString("name"));
                        account.setMoney(resultSet.getDouble("money"));
                        return account;
                    }
                });
    }

}

