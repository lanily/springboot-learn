package com.hsiao.springboot.transaction.service;


import com.hsiao.springboot.transaction.entity.Account;

/**
 * @author hemingzhun
 * @date 2019/1/24
 */

public interface IsolationAccountService extends AccountService {
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
     * 隔离级别-读未提交测试
     */
    void readUncommittedTest();

    /**
     * 获取读未提交隔离级别下的用户账户列表
     */
    void getReadUncommittedAllList();

    /**
     * 隔离级别-读提交测试
     */
    void readCommittedTest();

    /**
     * 获取读提交隔离级别下的用户账户列表
     */
    void getReadCommittedAllList();

    /**
     * 隔离级别-可重复读测试
     */
    void repeatableReadTest();

    /**
     * 获取可重复读隔离级别下的用户账户列表
     */
    void getRepeatableAllList();

    /**
     * 隔离级别-串行化测试
     */
    void serializableTest();

    /**
     * 获取串行化隔离级别下的用户账户列表
     */
    void getSerializableAllList();
}
