/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: IndexController Author: xiao Date: 2020/3/31 10:37 下午
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.template.thymeleaf.controller;

import cn.hutool.core.util.ObjectUtil;
import com.hsiao.springboot.template.thymeleaf.entity.User;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 主页控制器
 *
 * @projectName springboot-parent
 * @title: IndexController
 * @description: TODO
 * @author xiao
 * @create 2020/3/31
 * @since 1.0.0
 */
@Slf4j
@Controller
public class IndexController {

  @RequestMapping(value = {"", "/"})
  public ModelAndView index(HttpServletRequest request) {
    ModelAndView mv = new ModelAndView();

    User user = (User) request.getSession().getAttribute("user");
    if (ObjectUtil.isNull(user)) {
      mv.setViewName("login");
    } else {
      mv.setViewName("index");
      mv.addObject(user);
    }

    return mv;
  }

  @RequestMapping("/hello")
  public String hello() {
    return "page/hello";
  }

  @RequestMapping("/if")
  public String iF() {
    return "page/if";
  }

  @RequestMapping("/replace")
  public String replace() {
    return "page/replace";
  }

  @RequestMapping("/fragment")
  public String fragment() {
    return "page/fragment";
  }

  @RequestMapping("/layout")
  public String layout() {
    return "page/layout";
  }

  @RequestMapping("/home")
  public String home() {
    return "page/home";
  }
}
