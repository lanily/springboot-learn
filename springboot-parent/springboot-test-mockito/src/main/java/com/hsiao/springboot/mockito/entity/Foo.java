package com.hsiao.springboot.mockito.entity;


/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: Foo
 * @description: TODO
 * @author xiao
 * @create 2021/9/5
 * @since 1.0.0
 */
public class Foo {

    private Bar bar;

    public int sum(int a, int b) {
        return bar.add(a, b);
    }
}

