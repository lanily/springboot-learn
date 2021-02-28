/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: SessionControllerTest Author: xiao Date: 2020/3/29
 * 2:46 下午 History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.session.redis.web;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试session
 * @projectName springboot-session-redis
 * @title: SessionControllerTest
 * @description: TODO
 * @author xiao
 * @create 2020/3/29
 * @since 1.0.0
 */
@RestController
@RequestMapping("/spring")
public class SessionControllerTest {

    private TestRestTemplate testRestTemplate;
    private TestRestTemplate testRestTemplateWithAuth;
    private String testUrl = "http://localhost:8080/";

    @Before
    public void clearRedisData() {
        testRestTemplate = new TestRestTemplate();
        testRestTemplateWithAuth = new TestRestTemplate("admin", "admin", null);
    }

  @Value("${server.port}")
  private Integer projectPort;

  @GetMapping("/createSession")
  public String createSession(HttpSession session, String name) {
    session.setAttribute("name", name);
    return "当前项目端口：" + projectPort + " 当前sessionId：" + session.getId() + " 在Session中存入成功！";
  }

  @GetMapping("/getSession")
  public String getSession(HttpSession session) {
    return "当前项目端口："
        + projectPort
        + " 当前sessionId："
        + session.getId()
        + "  获取的姓名："
        + session.getAttribute("name");
  }


    @Test
    public void testUnauthenticatedCantAccess() {
        ResponseEntity<String> result = testRestTemplate.getForEntity(testUrl, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }


}
