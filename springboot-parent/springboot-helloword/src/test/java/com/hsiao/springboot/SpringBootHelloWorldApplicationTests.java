/**
 * Copyright (C), 2015-2019, XXX有限公司 FileName: ApplicationTests Author:   xiao Date:     2019/11/13
 * 9:50 PM History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

package com.hsiao.springboot;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hsiao.springboot.controller.HelloWorldController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * spring boot test 测试入口 <br>
 *
 * @author xiao
 * @projectName spring-boot-learn
 * @title: ApplicationTests
 * @description: TODO
 * @create 2019/11/13
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootHelloWorldApplicationTests {

    private MockMvc mvc;

    /**
     * 使用MockServletContext来构建一个空的WebApplicationContext，这样我们创建的HelloWorldController就可以在@Before函数中创建并传递到MockMvcBuilders.standaloneSetup()函数中。
     */
    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new HelloWorldController()).build();
    }

    @Test
    public void getHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Hello,World!")));
    }
}

