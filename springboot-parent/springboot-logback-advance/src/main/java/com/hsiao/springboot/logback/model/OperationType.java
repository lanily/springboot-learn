/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: OperationType Author:   xiao Date:     2020/11/20
 * 21:41 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.logback.model;


/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: OperationType
 * @description: TODO
 * @author xiao
 * @create 2020/11/20
 * @since 1.0.0
 */
public enum OperationType {
    /**
     * 操作类型
     */
    UNKNOWN("unknown"),
    DELETE("delete"),
    SELECT("select"),
    UPDATE("update"),
    INSERT("insert");

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    OperationType(String s) {
        this.value = s;
    }
}

