package com.hsiao.springboot.transaction.dao.impl;


import com.hsiao.springboot.transaction.dao.AccountDao;
import com.hsiao.springboot.transaction.entity.Account;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * AccountRepository
 *
 * @projectName springboot-parent
 * @title: AccountRepository
 * @description: TODO
 * @author xiao
 * @create 2021/2/27
 * @since 1.0.0
 */
@Repository  // @Component
public class AccountRepository implements AccountDao {

    // 使用JdbcTemplate可以简化对数据库的操作
    private JdbcTemplate jdbcTemplate;

    // 这个@Autowired一定要写到set方法上，因为和普通的set方法不同，这里的参数不是JdbcTemplate，而是DataSource
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void inMoney(String in, Double money) {
        this.jdbcTemplate.update(
                "update account set money = (money + ?) where name = ?",
                money, in);
    }

    @Override
    public void outMoney(String out, Double money) {
        this.jdbcTemplate.update(
                "update account set money = (money - ?) where name = ?",
                money, out);
    }

    @Override
    public Double query(String name) {
        return this.jdbcTemplate.queryForObject(
                "select money from account where name = ?",
                Double.class,
                name
        );
    }


    @Override
    public void insert(Account account) {
        this.jdbcTemplate
                .update("insert into account (name,money) values(?,?)", account.getName(),
                        account.getMoney());
    }

    @Override
    public void delete(int id) {
        this.jdbcTemplate.update("delete from account where id=?", id);
    }

    @Override
    public void update(Account account) {
        this.jdbcTemplate
                .update("update account set name=?,money=? where id=?", account.getName(),
                        account.getMoney(), account.getId());
    }

    @Override
    public List<Account> getAllList() {
        return this.jdbcTemplate
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

