package com.hsiao.springboot.transaction.service;

import com.hsiao.springboot.transaction.entity.Account;
import java.util.List;

public interface AccountService {
    /**
     * 转账功能
     *
     * @param out   : 转出账户
     * @param in    : 转入账户
     * @param money : 转账金额
     */
    public void transfer(String out, String in, Double money);

    /**
     * 根据账户名查询账户的金额
     *
     * @param name : 账户名
     * @return 账户余额
     */
    public Double query(String name);

    /**
     * 获取所有用户账户信息<br/>
     * <b>注意：数据量大的情况下必须分页，这里仅做测试</b>
     *
     * @return 用户账户信息集合List
     */
    List<Account> getAllList();

    void setNeedException(boolean needException);

}
