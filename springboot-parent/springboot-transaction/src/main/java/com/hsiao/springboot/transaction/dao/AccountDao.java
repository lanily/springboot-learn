package com.hsiao.springboot.transaction.dao;


import com.hsiao.springboot.transaction.entity.Account;
import java.util.List;

public interface AccountDao {

    /**
     * 加钱
     *
     * @param in    : 加钱账户
     * @param money : 增加的金额
     */
    public void inMoney(String in, Double money);

    /**
     * 扣钱
     *
     * @param out   : 扣钱账户
     * @param money : 减少的金额
     */
    public void outMoney(String out, Double money);

    /**
     * 根据账户名查询账户的金额
     *
     * @param name : 账户名
     * @return 账户余额
     */
    public Double query(String name);

    /**
     * 新增账户
     *
     * @param account
     */
    void insert(Account account);

    /**
     * 销户
     *
     * @param id
     */
    void delete(int id);

    /**
     * 更新账户
     *
     * @param account
     */
    void update(Account account);

    /**
     * 获取所有用户账户信息<br/>
     * <b>注意：数据量大的情况下必须分页，这里仅做测试</b>
     *
     * @return 用户账户信息集合List
     */
    List<Account> getAllList();
}
