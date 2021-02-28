/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: IndexCalculated Author:   xiao Date:     2020/11/14
 * 16:05 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.batch.insert.entity;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: IndexCalculated
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
@Table(name = "index_calculated")
@Entity
public class IndexCalculated implements Serializable {

    @Id
    @Column(name = "index_code")
    private String indexCode;

    @Column(name = "update_time")
    private String updateTime;

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}


