package com.hsiao.springboot.swagger.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * 广告模型
 *
 * @projectName springboot-parent
 * @title: Ad
 * @description: 广告模型
 * @author xiao
 * @create 2021/9/12
 * @since 1.0.0
 */
@ApiModel("广告模型")
public class Ad {

    @ApiModelProperty("标题")
    private String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ApiModelProperty("图片url")
    private String imgurl;

    public String getImgurl() {
        return this.imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

}
