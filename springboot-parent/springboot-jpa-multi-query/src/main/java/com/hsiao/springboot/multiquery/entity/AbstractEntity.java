package com.hsiao.springboot.multiquery.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author hsiao
 */
@MappedSuperclass
public abstract class AbstractEntity {
    @Id @GeneratedValue
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
