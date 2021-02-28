package com.hsiao.springboot.transaction.service;


/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: AbstractAcountService
 * @description: TODO
 * @author xiao
 * @create 2021/2/27
 * @since 1.0.0
 */
public abstract class AbstractAccountService implements AccountService{

    protected boolean needException = false;

    @Override
    public void setNeedException(boolean needException) {
         this.needException = needException;
    }
}

