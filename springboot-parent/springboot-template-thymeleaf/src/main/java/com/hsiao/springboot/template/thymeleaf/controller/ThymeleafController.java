/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: ThymeleafController Author:   xiao Date:     2020/4/2
 * 10:39 下午 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.template.thymeleaf.controller;


import com.hsiao.springboot.template.thymeleaf.dao.DepartmentDao;
import com.hsiao.springboot.template.thymeleaf.dao.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * @projectName springboot-parent
 * @title: ThymeleafController
 * @description: TODO
 * @author xiao
 * @create 2020/4/2
 * @since 1.0.0
 */
@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {

    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private EmployeeDao employeeDao;

    @RequestMapping("/")
    public String demo() {
        return "thymeleaf/index";
    }

    @RequestMapping("/demo1")
    public String demo1(Map<String, Object> map) {
        map.put("departments", departmentDao.getAll());
        map.put("employees", employeeDao.getAll());
        return "thymeleaf/demo1";
    }

    @RequestMapping("/demo2")
    public String demo2(Map<String, Object> map) {
        map.put("departments", departmentDao.getAll());
        map.put("employees", employeeDao.getAll());
        return "thymeleaf/demo2";
    }

    @RequestMapping("/demo3")
    public String demo3(Map<String, Object> map) {
        map.put("departments", departmentDao.getAll());
        map.put("employees", employeeDao.getAll());
        return "thymeleaf/demo3";
    }

    @RequestMapping("/demo4")
    public String demo4(Map<String, Object> map) {
        map.put("departments", departmentDao.getAll());
        map.put("employees", employeeDao.getAll());
        return "thymeleaf/demo4";
    }

    @RequestMapping("/demo5")
    public String demo5(Map<String, Object> map) {
        map.put("departments", departmentDao.getAll());
        map.put("employees", employeeDao.getAll());
        return "thymeleaf/demo5";
    }
}
