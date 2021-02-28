/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: UserController Author: xiao Date: 2020/3/29 6:57 下午
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.validate.controller;

import com.hsiao.springboot.validate.model.User;
import com.hsiao.springboot.validate.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户页面处理器
 *
 * @projectName springboot-parent
 * @title: UserController
 * @description: TODO
 * @author xiao
 * @create 2020/3/29
 * @since 1.0.0
 */
@Controller
@RequestMapping("/users") // 通过这里配置使下面的映射都在 /users
@Slf4j
public class UserController {
  @Autowired UserService userService; // 用户服务层

  /** 获取用户列表 处理 "/users" 的 GET 请求，用来获取用户列表 通过 @RequestParam 传递参数，进一步实现条件查询或者分页查询 */
  @RequestMapping(method = RequestMethod.GET)
  public String getUserList(ModelMap map) {
    map.addAttribute("userList", userService.findAll());
    return "userList";
  }

  /** 显示创建用户表单 */
  @RequestMapping(value = "/create", method = RequestMethod.GET)
  public String createUserForm(ModelMap map) {
    map.addAttribute("user", new User());
    map.addAttribute("action", "create");
    return "userForm";
  }

  /** 创建用户 处理 "/users" 的 POST 请求，用来获取用户列表 通过 @ModelAttribute 绑定参数，也通过 @RequestParam 从页面中传递参数 */
  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public String postUser(@ModelAttribute User user) {
    userService.insertByUser(user);
    return "redirect:/users/";
  }

  /**
   * 显示需要更新用户表单 处理 "/users/{id}" 的 GET 请求，通过 URL 中的 id 值获取 User 信息 URL 中的 id ，通过 @PathVariable 绑定参数
   */
  @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
  public String getUser(@PathVariable Long id, ModelMap map) {
    map.addAttribute("user", userService.findById(id));
    map.addAttribute("action", "update");
    return "userForm";
  }

  /** 处理 "/users/{id}" 的 PUT 请求，用来更新 User 信息 */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String putUser(@ModelAttribute User user) {
    userService.update(user);
    return "redirect:/users/";
  }

  /** 处理 "/users/{id}" 的 GET 请求，用来删除 User 信息 */
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
  public String deleteUser(@PathVariable Long id) {

    userService.delete(id);
    return "redirect:/users/";
  }
}
