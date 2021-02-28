package com.hsiao.springboot.logback.config;

import com.hsiao.springboot.logback.interceptor.RequestIdTraceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description  web配置
 * @Author xiao
 * @Since 2019/11/28
 **/
@Configuration
public class WebAppConfig implements WebMvcConfigurer {
    @Autowired
    RequestIdTraceInterceptor requestIdTraceInterceptor;

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加requestId
        registry.addInterceptor(requestIdTraceInterceptor);
    }
}
