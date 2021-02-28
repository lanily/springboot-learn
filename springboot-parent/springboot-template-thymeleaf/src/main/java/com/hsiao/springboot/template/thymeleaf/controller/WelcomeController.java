/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: WelcomeController Author: xiao Date: 2020/4/1 10:18
 * 下午 History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.template.thymeleaf.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * Welcome
 *
 * @projectName springboot-parent
 * @title: WelcomeController
 * @description: TODO
 * @author xiao
 * @create 2020/4/1
 * @since 1.0.0
 */
@Controller
@RequestMapping("/welcome")
public class WelcomeController {

  // inject via application.properties
  @Value("${welcome.message}")
  private String message;

  private List<String> tasks = Arrays.asList("a", "b", "c", "d", "e", "f", "g");

  @GetMapping("/")
  public String main(Model model) {
    model.addAttribute("message", message);
    model.addAttribute("tasks", tasks);

    return "welcome"; // view
  }

  // /hello?name=kotlin
  @GetMapping("/hello")
  public String mainWithParam(
      @RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {

    model.addAttribute("message", name);

    return "welcome"; // view
  }
}
