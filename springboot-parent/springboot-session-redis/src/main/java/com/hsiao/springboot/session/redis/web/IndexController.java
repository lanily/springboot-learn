/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: IndexController Author: xiao Date: 2020/3/29 1:53 下午
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.session.redis.web;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @projectName springboot-learn
 * @title: IndexController
 * @description: TODO
 * @author xiao
 * @create 2020/3/29
 * @since 1.0.0
 */
@Controller
public class IndexController {
  Logger logger = LoggerFactory.getLogger(IndexController.class);

  @Value("${server.port}")
  String port;

  @GetMapping("/")
  public String getSession(Model model, HttpSession session) {
    session.setAttribute("port", port);
    @SuppressWarnings("unchecked")
    List<String> messages = (List<String>) session.getAttribute("MY_SESSIONS");

    if (messages == null) {
      messages = new ArrayList<>();
    }
    model.addAttribute("messages", messages);

    return "index";
  }

  @PostMapping("/setSession")
  public String addSession(@RequestParam("message") String message, HttpServletRequest request) {
    logger.info("服务端口号为: " + request.getServerPort());

    @SuppressWarnings("unchecked")
    List<String> messages = (List<String>) request.getSession().getAttribute("MY_SESSIONS");
    if (messages == null) {
      messages = new ArrayList<>();
      request.getSession().setAttribute("MY_SESSIONS", messages);
    }
    messages.add(message);
    request.getSession().setAttribute("MY_SESSIONS", messages);
    return "redirect:/";
  }

  @PostMapping("/destroy")
  public String destroySession(HttpServletRequest request) {
    request.getSession().invalidate();
    return "redirect:/";
  }
}
