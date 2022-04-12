package com.hsiao.springboot.websocket.chat.controller;

import com.hsiao.springboot.websocket.chat.model.User;
import java.util.Random;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: LoginController
 * @description: TODO
 * @author xiao
 * @create 2022/4/25
 * @since 1.0.0
 */
@Controller
public class LoginController {

  @RequestMapping("/validate")
  public String validate(
      @RequestParam("username") String username,
      @RequestParam("password") String password,
      HttpSession httpSession) {
    if (StringUtils.isNotBlank(username)) {
      return "login";
    }

    if (StringUtils.isNotBlank(password)) {
      httpSession.setAttribute("id", new Random().nextInt(10));
      return "chatroom";
    } else {
      return "404";
    }
  }

  @RequestMapping("/login")
  public String login(){
    return "login";
  }

  @RequestMapping("/logout")
  public String logout(HttpSession httpSession){
    return "login";
  }

  @RequestMapping(value="/username", method = RequestMethod.GET)
  @ResponseBody
  public User getCurrentUser(HttpSession httpSession){
    Integer id = (Integer) httpSession.getAttribute("id");
    return new User(id, "id-" +id);
  }
}
