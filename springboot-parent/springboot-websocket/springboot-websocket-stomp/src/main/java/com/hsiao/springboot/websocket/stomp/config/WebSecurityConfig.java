package com.hsiao.springboot.websocket.stomp.config;


import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * https://github.com/salmar/spring-websocket-chat
 * https://javamana.com/2021/04/20210408114553591d.html
 * @projectName springboot-parent
 * @title: WebSecurityConfig
 * @description: TODO
 * @author xiao
 * @create 2022/4/21
 * @since 1.0.0
 */
@Configuration
// 开启Spring Security的功能
@EnableWebSecurity
// prePostEnabled属性决定Spring Security在接口前注解是否可用@PreAuthorize,
// @PostAuthorize等注解,设置为true,会拦截加了这些注解的接口
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //    private static final String SECURE_ADMIN_PASSWORD = "admin";

    // 认证成功结果处理器
//    @Resource
//    private AuthenticationSuccessHandler loginSuccessHandler;
    // 认证失败结果处理器
//    @Resource
//    private AuthenticationFailureHandler loginFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 1、配置权限认证
                .authorizeRequests()
                // 配置不拦截路由
                .antMatchers("/500").permitAll()
                .antMatchers("/403").permitAll()
                .antMatchers("/404").permitAll()
                .antMatchers("/login.html").permitAll()
                //满足以下路径的无条件允许访问
                .antMatchers("/webjars/**", "/css/**", "/js/**", "/fonts/**").permitAll()
//                .antMatchers("/css/**", "/js/**", "/fonts/**").permitAll()
//                .antMatchers("/500", "/403", "/404", "/login")
//                .permitAll()
                .antMatchers("/websocket").hasRole("ADMIN")
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                // 任何其它请求
//                .anyRequest()
                // 都需要身份认证
//                .authenticated()
                .and()
                // 2、登录配置表单认证方式
                .formLogin()
                .loginPage("/login.html")
                // 设置登录账号参数，与表单参数一致
                .usernameParameter("username")
                // 设置登录密码参数，与表单参数一致
                .passwordParameter("password")
// 告诉Spring Security在发送指定路径时处理提交的凭证，默认情况下，将用户重定向回用户来自的页面。登录表单form中action的地址，也就是处理认证请求的路径，
// 只要保持表单中action和HttpSecurity里配置的loginProcessingUrl一致就可以了，也不用自己去处理，它不会将请求传递给Spring MVC和您的控制器，所以我们就不需要自己再去写一个/login的控制器接口了
                // 配置默认登录入口
                .loginProcessingUrl("/login")
                // 登录成功后默认的跳转页面路径
                .defaultSuccessUrl("/index.html")
//                .failureUrl("/login?error=true")
                .failureUrl("/404.html")
                // 使用自定义的成功结果处理器
//                .successHandler(loginSuccessHandler)
                // 使用自定义失败的结果处理器
//                .failureHandler(loginFailureHandler)
                .and()
                // 3、注销
                .logout()
                .logoutSuccessUrl("/index.html")
//                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                .permitAll()
                .and()
                // 4、session管理
                .sessionManagement()
                // 失效后跳转到登陆页面
                .invalidSessionUrl("/login")
// 单用户登录，如果有一个登录了，同一个用户在其他地方登录将前一个剔除下线
//.maximumSessions(1).expiredSessionStrategy(expiredSessionStrategy())
// 单用户登录，如果有一个登录了，同一个用户在其他地方不能登录
// .maximumSessions(1).maxSessionsPreventsLogin(true) ;
                .and()
                // 5、禁用跨站csrf攻击防御
                .csrf()
                .disable();
        // 解决不允许显示在iframe的问题
        http.headers().frameOptions().disable();
        http.headers().cacheControl();

    }

/*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
       auth.authenticationProvider(new AuthenticationProvider() {
            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }

            @Override
            public Authentication authenticate(Authentication authentication)
                    throws AuthenticationException {
                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

                List<GrantedAuthority> authorities =
                        SECURE_ADMIN_PASSWORD.equals(token.getCredentials()) ?
                                AuthorityUtils.createAuthorityList("ROLE_ADMIN") : null;

                return new UsernamePasswordAuthenticationToken(token.getName(),
                        token.getCredentials(), authorities);
            }
        });

    }
    */


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 基于内存的方式，创建两个用户admin/123456，user/123456
         */
        auth.inMemoryAuthentication()
                // 用户名
                .withUser("admin")
                // 密码
                .password(passwordEncoder().encode("123456"))
                // 角色
                .roles("ADMIN");
        auth.inMemoryAuthentication()
                // 用户名
                .withUser("user")
                // 密码
                .password(passwordEncoder().encode("123456"))
                // 角色
                .roles("USER");
    }

    /*@Override
    public void configure(WebSecurity web) throws Exception {
        // 放行static下的所有文件
//        web.ignoring().antMatchers("/**");
        // 配置静态文件不需要认证
//         放行static/static下的所有文件rs("/static/**");
        web.ignoring().antMatchers("/static/**");
//        web.ignoring().antMatchers("/js/**", "/css/**","/images/**");
    }*/

    /**
     * 指定加密方式
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用BCrypt加密密码
        return new BCryptPasswordEncoder();
    }
}
