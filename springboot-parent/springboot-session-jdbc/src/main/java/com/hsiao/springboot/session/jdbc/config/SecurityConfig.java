/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: SecurityConfig Author:   xiao Date:     2020/3/29
 * 1:48 下午 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.session.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @projectName springboot-learn
 * @title: SecurityConfig
 * @description: TODO
 * @author xiao
 * @create 2020/3/29
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .formLogin()
                .and()
                .authorizeRequests()
                .antMatchers("/")
                .hasRole("ADMIN")
                .antMatchers("/login")
                .permitAll()
                // 设置session过期时间以及加盐
                .and()
                .rememberMe()
                .tokenValiditySeconds(1000000)
                .key("Beatles")
                .and();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(new BCryptPasswordEncoder().encode("admin"))
                .roles("ADMIN");    }
}
