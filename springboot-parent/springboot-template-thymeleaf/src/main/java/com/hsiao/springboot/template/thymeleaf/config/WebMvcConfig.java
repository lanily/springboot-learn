/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: WebMvcConfig Author:   xiao Date:     2020/4/2 9:45
 * 下午 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.template.thymeleaf.config;


/**
 * 使用 WebMvcConfigurer 扩展 SpringMVC的功能
 * @projectName springboot-parent
 * @title: WebMvcConfig
 * @description: TODO
 * @author xiao
 * @create 2020/4/2
 * @since 1.0.0
 */
/*public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/replace.html").setViewName("login");
        registry.addViewController("/index").setViewName("login");
        registry.addViewController("/main.html").setViewName("dashboard");

    }

    *//**
     * 注册定制组件
     *//*
    @Bean
    public LocaleResolver localeResolver() {
        return new CustomLocaleResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        //排除静态资源的拦截：*.css , *.js
//        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/replace.html", "/index", "/user/login", "/asserts/**","/webjars/**");

        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/main.html", "/emps", "/emp/*");
    }

}*/

