package com.hsiao.springboot.test.entity;


import lombok.Getter;
import lombok.Setter;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: PageParam
 * @description: TODO
 * @author xiao
 * @create 2021/7/22
 * @since 1.0.0
 */
@Getter
@Setter
public class PageParam {
    private long pageNo;
    private long pageSize;

    public static PageParam create() {
        return create(1L, 10L);
    }
    public static PageParam create(long pageNo, long pageSize) {
        return new PageParam(pageNo, pageSize);
    }
    public PageParam(long pageNo, long pageSize) {
        if (pageNo < 1L) {
            this.pageNo = 1L;
        }
        this.pageNo = pageNo < 1L ? 1L : pageNo;
        this.pageSize = pageSize <= 0L ? 10L : pageSize;
    }
}
