/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: IndexController Author: xiao Date: 2020/3/29 1:53 下午
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.session.jdbc.web;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @projectName springboot-learn
 * @title: IndexController
 * @description: TODO
 * @author xiao
 * @create 2020/3/29
 * @since 1.0.0
 */
@RequestMapping("session")
public class IndexController {

  @GetMapping("/")
  public String getSession(Model model, HttpSession session) {
    @SuppressWarnings("unchecked")
    List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

    if (messages == null) {
      messages = new ArrayList<>();
    }
    model.addAttribute("sessionMessages", messages);

    return "index";
  }

  @PostMapping("/addSession")
  public String addSession(@RequestParam("msg") String msg, HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    List<String> messages = (List<String>) request.getSession().getAttribute("MY_SESSION_MESSAGES");
    if (messages == null) {
      messages = new ArrayList<>();
      request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);
    }
    messages.add(msg);
    request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);
    return "redirect:/";
  }

  @PostMapping("/destroy")
  public String destroySession(HttpServletRequest request) {
    request.getSession().invalidate();
    return "redirect:/";
  }
}
