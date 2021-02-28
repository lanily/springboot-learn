/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: LoginHandlerInterceptor Author:   xiao Date:
 * 2020/4/2 9:42 下午 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.template.thymeleaf.config;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 自定义登录拦截器
 * @projectName springboot-parent
 * @title: LoginHandlerInterceptor
 * @description: TODO
 * @author xiao
 * @create 2020/4/2
 * @since 1.0.0
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {

    /**
     * 目标方法执行前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            //未登录，返回登录页面
            //request.setAttribute("msg", "没有权限请先登录");
            request.getRequestDispatcher("/replace.html").forward(request, response);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

