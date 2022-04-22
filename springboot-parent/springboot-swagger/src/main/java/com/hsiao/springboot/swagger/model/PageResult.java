package com.hsiao.springboot.swagger.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 *  {
 *      "list": [],
 *         "pagination": {
 *             "total": 100,
 *             "currentPage": 1,
 *             "prePageCount": 10
 *         }
 *  }
 * 分页返回数据
 *
 * @projectName springboot-parent
 * @title: PageResult
 * @description: 分页返回数据
 * @author xiao
 * @create 2021/9/12
 * @since 1.0.0
 */
@ApiModel("分页返回数据")
public class PageResult<T> {

    @ApiModelProperty("列表，分页中的数据")
    private List<T> list;

    @ApiModelProperty("页数信息")
    private PaginationResult pagination;

    public PageResult() {
    }

    public void setList(List list) {
        this.list = list;
    }
    public List<T> getList() {
        return this.list;
    }

    public void setPagination(PaginationResult pagination) {
        this.pagination = pagination;
    }
    public PaginationResult getPagination() {
        return this.pagination;
    }
}
