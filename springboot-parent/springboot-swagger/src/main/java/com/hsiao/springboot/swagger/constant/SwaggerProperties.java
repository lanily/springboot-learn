package com.hsiao.springboot.swagger.constant;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * swagger配置类SwaggerProperties
 *
 * @projectName springboot-parent
 * @title: SwaggerProperties
 * @description: swagger配置类SwaggerProperties
 * @author xiao
 * @create 2021/9/12
 * @since 1.0.0
 */
@Component
@ConfigurationProperties("swagger")//通过该属性可以获得application.yml中的swagger值
public class SwaggerProperties {

    /**
     * 是否开启swagger，生产环境一般关闭，所以这里定义一个变量
     */
    private Boolean enable;//对应application.yml中的swagger.enable

    /**
     * 项目应用名
     */
    private String applicationName;//对应application.yml中的swagger.application-name

    /**
     * 项目版本信息
     */
    private String applicationVersion;//对应application.yml中的swagger.application.version

    /**
     * 项目描述信息
     */
    private String applicationDescription;//对应application.yml中的swagger.application-description

    /**
     * 接口调试地址
     */
    private String tryHost;//对应application.yml中的swagger.try-host

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }

    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    public String getTryHost() {
        return tryHost;
    }

    public void setTryHost(String tryHost) {
        this.tryHost = tryHost;
    }
}
