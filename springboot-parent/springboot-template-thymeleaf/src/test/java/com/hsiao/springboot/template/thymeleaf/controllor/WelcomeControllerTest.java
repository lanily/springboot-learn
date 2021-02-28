/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: WelcomeControllerTest Author: xiao Date: 2020/4/1
 * 10:20 下午 History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.template.thymeleaf.controllor;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.hsiao.springboot.template.thymeleaf.controller.WelcomeController;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.ModelAndView;


/**
 * welcome controller test case
 *
 * @projectName springboot-parent
 * @title: WelcomeControllerTest
 * @description: TODO
 * @author xiao
 * @create 2020/4/1
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = WelcomeController.class)
public class WelcomeControllerTest {

  List<String> expectedList = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
  @Autowired private MockMvc mockMvc;

  @Test
  public void main() throws Exception {
    ResultActions resultActions =
        mockMvc
            .perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("welcome"))
            .andExpect(model().attribute("message", equalTo("Mkyong")))
            .andExpect(model().attribute("tasks", is(expectedList)))
            .andExpect(content().string(containsString("Hello, Mkyong")));

    MvcResult mvcResult = resultActions.andReturn();
    ModelAndView mv = mvcResult.getModelAndView();
    //
  }

  // Get request with Param
  @Test
  public void hello() throws Exception {
    mockMvc
        .perform(get("/hello").param("name", "I Love Kotlin!"))
        .andExpect(status().isOk())
        .andExpect(view().name("welcome"))
        .andExpect(model().attribute("message", equalTo("I Love Kotlin!")))
        .andExpect(content().string(containsString("Hello, I Love Kotlin!")));
  }
}
