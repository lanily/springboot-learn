package com.hsiao.springboot.transaction.entity;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: Account
 * @description: TODO
 * @author xiao
 * @create 2021/2/27
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    /**
     * 账户ID
     */
    private int id;
    /**
     * 账户名
     */
    private String name;
    /**
     * 账户金额
     */
    private double money;

    public Account(String name, double money) {
        this.name = name;
        this.money = money;
    }
}

