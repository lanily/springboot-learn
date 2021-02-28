package com.hsiao.security.properties;

import com.hsiao.security.constants.SecurityConstants;
import com.hsiao.security.validate.code.enums.LoginResponseType;

import lombok.Data;

@Data
public class BrowserProperties {

    private SessionProperties session = new SessionProperties();

    /**
     * 默认登录页面
     */
    private String loginPage = SecurityConstants.DEFAULT_LOGIN_PAGE_URL;

    /**
     * 默认登录方式
     */
    private LoginResponseType loginType = LoginResponseType.JSON;

    /**
     * 默认记住我的时长
     */
    private int rememberMeSeconds = 60;

}
