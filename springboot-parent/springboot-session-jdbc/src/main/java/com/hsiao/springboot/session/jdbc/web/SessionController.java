/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: SessionController Author: xiao Date: 2020/3/29 2:40
 * 下午 History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.session.jdbc.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @projectName springboot-session-redis
 * @title: SessionController
 * @description: TODO
 * @author xiao
 * @create 2020/3/29
 * @since 1.0.0
 */
@RestController
@RequestMapping(path = "/jdbc")
public class SessionController {

  @RequestMapping("/session")
  public Object springSession(
      @RequestParam("username") String username, HttpServletRequest request, HttpSession session) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().contains("session")) {
          System.out.println(cookie.getName() + "=" + cookie.getValue());
        }
      }
    }

    Object value = session.getAttribute("username");
    if (value == null) {
      System.out.println("用户不存在");
      session.setAttribute("username", "{username: '" + username + "', age: 28}");
    } else {
      System.out.println("用户存在");
    }

    return "username=" + value;
  }

  /**
   * session测试
   *
   * @param request
   * @return
   */
  @RequestMapping(value = "/session", method = RequestMethod.GET)
  public Map<String, String> addSession(HttpServletRequest request) {
    String sessionId = request.getSession().getId();
    String requestURI = request.getRequestURI() + ":" + request.getServerPort();
    // 向session中保存用户信息 key规则： user + "_" + uid
    request.getSession().setAttribute("user_1", "{uid:1,username:11@qq.com}");

    Map<String, String> sessionInfoMap = new HashMap<>(2);
    sessionInfoMap.put("sessionId", sessionId);
    sessionInfoMap.put("requestURI", requestURI);
    return sessionInfoMap;
  }

  /**
   * session测试
   *
   * @param request
   * @return
   */
  @RequestMapping(value = "/getSession", method = RequestMethod.GET)
  public Map<String, String> getSession(HttpServletRequest request) {
    String sessionId = request.getSession().getId();
    String requestURI = request.getRequestURI() + ":" + request.getServerPort();

    Map<String, String> sessionInfoMap = new HashMap<>(2);
    // 获取session中uid为1的用户的信息
    String user_1 = (String) request.getSession().getAttribute("user_1");

    sessionInfoMap.put("sessionId", sessionId);
    sessionInfoMap.put("requestURI", requestURI);
    sessionInfoMap.put("user_1", user_1);
    return sessionInfoMap;
  }

  @ResponseBody
  @RequestMapping(value = "/session1")
  public Map<String, Object> getSession1(HttpServletRequest request) {
    request.getSession().setAttribute("username", "guest");
    Map<String, Object> map = new HashMap();
    map.put("sessionId", request.getSession().getId());
    map.put("maxInactiveInterval", request.getSession().getMaxInactiveInterval() / 60);
    map.put("createTime", new Date(request.getSession().getCreationTime()));
    return map;
  }

  @ResponseBody
  @RequestMapping(value = "/get")
  public String get(HttpServletRequest request) {
    String userName = (String) request.getSession().getAttribute("username");
    return userName;
  }
}
