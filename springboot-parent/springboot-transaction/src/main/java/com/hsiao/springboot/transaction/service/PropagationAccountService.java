package com.hsiao.springboot.transaction.service;

import com.hsiao.springboot.transaction.entity.Account;

/**
 * @author hsiao
 * @date 2019/1/24
 */

public interface PropagationAccountService extends AccountService {
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
     * 事务传播机制-required测试
     */
    void propagationrequiredTest();

    /**
     * 事务传播机制-requires_new测试
     */
    void propagationrequires_newTest();

    /**
     * 事务传播机制-supports测试
     */
    void propagationsupportsTest();

    /**
     * 事务传播机制-not_supported测试
     */
    void propagationnot_supportedTest();

    /**
     * 事务传播机制-mandatory测试
     */
    void propagationmandatoryTest();

    /**
     * 事务传播机制-nested测试
     */
    void propagationnestedTest();

    /**
     * 事务传播机制-never测试
     */
    void propagationneverTest();
}
