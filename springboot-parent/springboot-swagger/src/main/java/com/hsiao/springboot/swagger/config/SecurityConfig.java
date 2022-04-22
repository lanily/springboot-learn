package com.hsiao.springboot.swagger.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: SecurityConfig
 * @description: TODO
 * @author xiao
 * @create 2021/9/12
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${swagger.access.iplist}")
    private String iplist;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //得到iplist列表
        String iprule = "";
        //hasIpAddress('10.0.0.0/16') or hasIpAddress('127.0.0.1/32')
        String[] splitAddress = iplist.split(",");
        for (String ip : splitAddress) {
            if (iprule.equals("")) {
                iprule = "hasIpAddress('" + ip + "')";
            } else {
                iprule += " or hasIpAddress('" + ip + "')";
            }
        }
        String swaggerrule = "hasAnyRole('ADMIN','DEV') and (" + iprule + ")";

        //login和logout
        http.formLogin()
                .defaultSuccessUrl("/home/session")
                .failureUrl("/login-error.html")
                .permitAll()
                .and()
                .logout();

        //匹配的页面，符合限制才可访问
        http.authorizeRequests()
                //.antMatchers("/actuator/**").hasIpAddress("127.0.0.1")
                //.antMatchers("/admin/**").access("hasRole('admin') and (hasIpAddress('127.0.0.1') or hasIpAddress('192.168.1.0/24') or hasIpAddress('0:0:0:0:0:0:0:1'))");
//                .antMatchers("/doc/**").access(swaggerrule)
//                .antMatchers("/**").access(swaggerrule)
                .antMatchers("/goods/**").hasAnyRole("ADMIN", "DEV");

        //剩下的页面，允许访问
        http.authorizeRequests().anyRequest().permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //添加两个账号用来做测试
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("ADMIN", "USER")
                .and()
                .withUser("hsiao")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("USER");
    }
}
