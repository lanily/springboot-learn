package com.hsiao.springboot.multiquery.entity;

import javax.persistence.Entity;

/**
 * 住址
 * @author hsiao
 */
@Entity
public class Address extends AbstractEntity {
    private String city;        // 市
    private String county;      // 县
    private Integer num;        // 号

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
