package com.hsiao.springboot.websocket.api.controller;


import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: LoginController
 * @description: TODO
 * @author xiao
 * @create 2022/4/22
 * @since 1.0.0
 */
@Controller
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @GetMapping("/")
    public String index () {
        // this is wrong
//        return "login";
//        return "login.html";
        return "redirect:/login.html";
    }

    /**
     * 判断用户是否登录
     *
     * @param request 请求对象，从中获取session里面的用户信息以判断用户是否登录
     * @return 结果对象，已经登录则结果为成功，且数据体为用户信息；否则结果为失败，数据体为空
     */
    @GetMapping("/islogin")
    public String isLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (Objects.nonNull(session)) {
            //从session里面获取用户信息
            String username = (String) session.getAttribute("SESSION_USERNAME");
            //如果从session中获取用户信息为空，则说明没有登录
            if (StringUtils.isBlank(username)) {
                logger.info("用户未登录！");
                return "redirect:/login.html";
            }
        }
        //若用户登录，利用里面的信息去数据库查找并进行比对，保证信息正确性
        return "redirect:success.html";
    }

    /**
     * 利用session进行登录验证
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */

    @PostMapping("/login")
    public String login(@RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            HttpServletRequest request, HttpServletResponse response) {
        // 登录认证，认证成功后将用户信息放到session中
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            HttpSession session = request.getSession();
//            HttpSession session = request.getSession(false);
            session.setAttribute("SESSION_USERNAME", username);
            // 一般直接保存user实体
            if (Objects.nonNull(session)) {
                session.setAttribute("SESSION_USERNAME", username);
            }
        }
        logger.info("{} 登录", username);
        return "redirect:websocket.html";
    }


    /**
     * 用户登出
     *
     * @param request 请求，用于操作session
     * @return 结果对象
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        //用户登出很简单，就是把session里面的用户信息设为null即可
        request.getSession().setAttribute("SESSION_USERNAME", null);
        logger.info("用户退出登录成功！");
        return "success";
//        return "redirect:index.html";
    }
}
