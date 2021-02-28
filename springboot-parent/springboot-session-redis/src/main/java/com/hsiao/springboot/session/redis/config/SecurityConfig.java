/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: SecurityConfig Author: xiao Date: 2020/3/29 1:48 下午
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.session.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
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
    // 设置登录,注销，表单登录不用拦截，其他请求要拦截
    http.authorizeRequests()
        .antMatchers("/")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .logout()
        .permitAll()
        .and()
        .formLogin()
        // 设置session过期时间以及加盐
        .and()
        .rememberMe()
        .tokenValiditySeconds(1000000)
        .key("Beatles");

    // 关闭默认的csrf认证
    http.csrf().disable();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    // 设置静态资源不要拦截
    web.ignoring().antMatchers("/js/**", "/css/**", "/images/**");
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    // java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"
    //      auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");

    /*
      inMemoryAuthentication 从内存中获取
      方式一、增加具体实例

      内存
             在inMemoryAuthentication()后面多了".passwordEncoder(new
             BCryptPasswordEncoder())",相当于登陆时用BCrypt加密方式对用户密码进行处理。
             以前的".password("123456")" 变成了 ".password(new BCryptPasswordEncoder().encode("123456"))"
             这相当于对内存中的密码进行Bcrypt编码加密。
             比对时一致，说明密码正确，允许登陆。

      数据库

             如果是注入userDetailsService
             注入userDetailsService的实现类
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());

        如果你用的是在数据库中存储用户名和密码，那么一般是要在用户注册时就使用BCrypt编码将用户密码加密处理后存储在数据库中。
        并且修改configure()方法，加入".passwordEncoder(new BCryptPasswordEncoder())"，
        保证用户登录时使用bcrypt对密码进行处理再与数据库中的密码比对。

    如果配置了spring security oauth2

        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory().withClient("client").secret("{noop}secret")
                    .authorizedGrantTypes("client_credentials", "password", "refresh_token").scopes("all");
        }
               */

    // 可以设置内存指定的登录的账号密码,指定角色
    // 不加.passwordEncoder(new MyPasswordEncoder())
    // 就不是以明文的方式进行匹配，会报错
    // auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN");

    // .passwordEncoder(new MyPasswordEncoder())。
    // 这样，页面提交时候，密码以明文的方式进行匹配。
    auth.inMemoryAuthentication()
        .passwordEncoder(new BCryptPasswordEncoder())
        .withUser("admin")
        .password(new BCryptPasswordEncoder().encode("admin"))
        .roles("ADMIN")
            .and()
            // for inMemory Authentication {noop} for plain text
            .withUser("user").password("{noop}user").roles("USER");

//      auth.inMemoryAuthentication()
//              .withUser("user").password("{noop}password").roles("USER")
//              .and()
//              .withUser("admin").password("{noop}password").roles("ADMIN");
  }
}
