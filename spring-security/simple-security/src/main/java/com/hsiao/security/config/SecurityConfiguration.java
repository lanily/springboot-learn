package com.hsiao.security.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * SpringSecurity核心功能:
 *
 * 认证（你是谁）
 * 授权（你能干什么）
 * 攻击防护（防止伪造身份）
 */
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 在内存中创建一个名为 "user" 的用户，密码为 "user"，拥有 "USER" 权限，密码使用BCryptPasswordEncoder加密
         */
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("user").password(new BCryptPasswordEncoder().encode("user")).roles("USER");
        /**
         * 在内存中创建一个名为 "admin" 的用户，密码为 "admin"，拥有 "USER" 和"ADMIN"权限
         */
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin").password(new BCryptPasswordEncoder().encode("admin")).roles("USER","ADMIN");
    }

    /**
     * 复写这个方法来配置 {@link HttpSecurity}. 
     * 通常，子类不能通过调用 super 来调用此方法，因为它可能会覆盖其配置。 默认配置为：
     * 
     * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
     * authorizeRequests()配置路径拦截，表明路径访问所对应的权限，角色，认证信息。
     * formLogin()对应表单认证相关的配置
     * logout()对应了注销相关的配置
     * httpBasic()可以配置basic登录
     * 匹配 "/","/index" 路径，不需要权限即可访问
     * 除了“/”,”/index”(首页),”/login”(登录),”/logout”(注销),之外，其他路径都需要认证。
     * 匹配 "/user" 及其以下所有路径，都需要 "USER" 权限
     * 匹配 "/admin" 及其以下所有路径，都需要 "ADMIN" 权限
     * 指定“/login”该路径为登录页面，当未认证的用户尝试访问任何受保护的资源时，都会跳转到“/login”。
     * 登录地址为 "/login"，登录成功默认跳转到页面 "/user"
     * 退出登录的地址为 "/logout"，退出成功后跳转到页面 "/login"
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() // 定义哪些URL需要被保护、哪些不需要被保护
                .antMatchers("/","/index","/error").permitAll()
                .antMatchers("/resources/**", "/signup", "/about").permitAll()  // 设置所有人都可以访问资源，登录，注册，关于页面
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
                .anyRequest().authenticated() // 任何请求,登录后可以访问
                .and()
                .formLogin() // 定义当需要用户登录时候，转到的登录页面。
                .usernameParameter("username")
                .passwordParameter("password")
                .failureForwardUrl("/login?error")
                .loginPage("/login") // 设置登录页面
                .defaultSuccessUrl("/user") // 设置登录成功后页面
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/index") // 设置退出成功后跳转页面
                .permitAll()
                .and()
                .httpBasic()
                .disable()
                .csrf().disable();     // 关闭csrf防护;
    }
}
