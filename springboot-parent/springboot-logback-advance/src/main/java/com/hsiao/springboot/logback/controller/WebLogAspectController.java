/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: WebLogAspectTest Author: xiao Date: 2020/11/20 21:27
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.logback.controller;

import com.hsiao.springboot.logback.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: WebLogAspectTest
 * @description: TODO
 * @author xiao
 * @create 2020/11/20
 * @since 1.0.0
 */
@RestController
public class WebLogAspectController {

  private static final Logger logger = LoggerFactory.getLogger(WebLogAspectController.class);

  /**
   * POST 方式接口测试
   *
   * @param user
   * @return
   */
  @PostMapping("/user")
  public User testPost(@RequestBody User user) {
    logger.info("testPost ...");
    return user;
  }

  /**
   * GET 方式接口测试
   *
   * @return
   */
  @GetMapping("/user")
  public String testGet(
      @RequestParam("username") String username, @RequestParam("password") String password) {
    logger.info("testGet ...");
    return "success";
  }

  /**
   * 单文件上传接口测试
   *
   * @return
   */
  @PostMapping("/file/upload")
  public String testFileUpload(@RequestParam("file") MultipartFile file) {
    logger.info("testFileUpload ...");
    return "success";
  }

  /**
   * 多文件上传接口测试
   *
   * @return
   */
  @PostMapping("/multiFile/upload")
  public String testMultiFileUpload(@RequestParam("file") MultipartFile[] file) {
    logger.info("testMultiFileUpload ...");
    return "success";
  }
}
