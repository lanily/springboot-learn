/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: EmployeeControllerTest Author:   xiao Date:
 * 2020/11/14 14:26 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.batch.insert;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: EmployeeControllerTest
 * @description: TODO
 * @author xiao
 * @create 2020/11/14
 * @since 1.0.0
 */
public class EmployeeControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenInsertingCustomers_thenCustomersAreCreated() throws Exception {
        this.mockMvc.perform(post("/employees"))
                .andExpect(status().isCreated());
    }

}

