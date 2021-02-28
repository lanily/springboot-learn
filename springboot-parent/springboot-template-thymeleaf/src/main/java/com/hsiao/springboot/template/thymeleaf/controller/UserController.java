/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: UserController Author: xiao Date: 2020/3/31 10:34 下午
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.template.thymeleaf.controller;

import com.hsiao.springboot.template.thymeleaf.dao.UserRepository;
import com.hsiao.springboot.template.thymeleaf.entity.User;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户页面控制器
 *
 * @projectName springboot-parent
 * @title: UserController
 * @description: TODO
 * @author xiao
 * @create 2020/3/31
 * @since 1.0.0
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
  @Autowired private UserRepository userRepository;

  @GetMapping("/login")
  public String login() {
    return "users/login";
  }

  @PostMapping("/login")
  public String login(
      String username, String password, Map<String, Object> map, HttpSession session) {
    if ("admin".equals(username) && "123".equals(password)) {
      session.setAttribute("user", username);
      return "users/index";
    }
    map.put("loginState", "login fail");
    return "users/login";
  }

  /**
   * 从 用户存储库 获取用户列表
   *
   * @return
   */
  private List<User> getUserlist() {
    return userRepository.listUser();
  }

  /**
   * 查询所用用户
   *
   * @return
   */
  @GetMapping
  public ModelAndView list(Model model) {
    model.addAttribute("userList", getUserlist());
    model.addAttribute("title", "用户管理");
    return new ModelAndView("users/list", "userModel", model);
  }

  /**
   * 根据id查询用户
   *
   * @return
   */
  @GetMapping("{id}")
  public ModelAndView view(@PathVariable("id") Long id, Model model) {
    User user = userRepository.getUserById(id);
    model.addAttribute("user", user);
    model.addAttribute("title", "查看用户");
    return new ModelAndView("users/view", "userModel", model);
  }

  /**
   * 获取 form 表单页面
   *
   * @return
   */
  @GetMapping("/form")
  public ModelAndView createForm(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("title", "创建用户");
    return new ModelAndView("users/form", "userModel", model);
  }

  /**
   * 新建用户
   *
   * @param user
   * @return
   */
  @PostMapping
  public ModelAndView create(User user) {
    user = userRepository.saveOrUpateUser(user);
    return new ModelAndView("redirect:/users");
  }

  /**
   * 删除用户
   *
   * @param id
   * @return
   */
  @GetMapping(value = "delete/{id}")
  public ModelAndView delete(@PathVariable("id") Long id, Model model) {
    userRepository.deleteUser(id);

    model.addAttribute("userList", getUserlist());
    model.addAttribute("title", "删除用户");
    return new ModelAndView("users/list", "userModel", model);
  }

  /** 修改用户 */
  @GetMapping(value = "modify/{id}")
  public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
    User user = userRepository.getUserById(id);

    model.addAttribute("user", user);
    model.addAttribute("title", "修改用户");
    return new ModelAndView("users/form", "userModel", model);
  }
}
