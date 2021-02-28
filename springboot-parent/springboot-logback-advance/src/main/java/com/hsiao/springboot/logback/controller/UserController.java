package com.hsiao.springboot.logback.controller;

import com.hsiao.springboot.logback.model.User;
import com.hsiao.springboot.logback.service.UserService;
import com.hsiao.springboot.logback.vo.ResponseResult;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * User控制器
 *
 * @author xiao
 * @since 2019/10/31
 */
@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired private UserService userService;

  /**
   * 查询单个用户
   *
   * @param userId
   * @return
   */
  @GetMapping("/{userId}")
  public ResponseResult<User> getUserById(@PathVariable long userId) {
    return ResponseResult.ok(userService.getUserById(userId));
  }

    /**
     * 访问路径 http://localhost:11000/user/findUserNameByTel?tel=1234567
     * @param tel 手机号
     * @return userName
     */
    @ResponseBody
    @RequestMapping("/findUserNameByTel")
    public String findUserNameByTel(@RequestParam("tel") String tel){
        return userService.findUserName(tel);
    }
  /**
   * 查询多个用户
   *
   * @return
   */
  @GetMapping()
  public ResponseResult<List<User>> getUsers() {
    return ResponseResult.ok(userService.getUsers());
  }

  /**
   * 添加多个用户
   *
   * @param users
   * @return
   */
  @PostMapping()
  public ResponseResult<String> addUsers(@RequestBody List<User> users) {
    if (userService.addUsers(users) > 0) {
      return ResponseResult.ok("users added");
    } else {
      return ResponseResult.err("ADD_USER_ERROR", "添加多个用户失败", "add exception");
    }
  }

  /**
   * 添加单个用户
   *
   * @param user
   * @return
   */
  @PostMapping("/user")
  public ResponseResult<String> addUser(@RequestBody User user) {
    if (userService.addUser(user) > 0) {
      return ResponseResult.ok("user added");
    } else {
      return ResponseResult.err("ADD_USER_ERROR", "添加用户失败", "add exception");
    }
  }

  /**
   * 更新单个用户
   *
   * @param user
   * @return
   */
  @PutMapping("/user")
  public ResponseResult<String> updateUser(@RequestBody User user) {
    if (userService.updateUser(user) > 0) {
      return ResponseResult.ok("user updated");
    } else {
      return ResponseResult.err("UPDATE_USER_ERROR", "更新用户失败", "update exception");
    }
  }

  /**
   * 删除单个用户
   *
   * @param userId
   * @return
   */
  @DeleteMapping("/user")
  public ResponseResult<String> deleteUser(long userId) {
    if (userService.deleteUser(userId) > 0) {
      return ResponseResult.ok("user deleted");
    } else {
      return ResponseResult.err("DELETE_USER_ERROR", "删除用户失败", "delete exception");
    }
  }
}
