package com.hsiao.springboot.validate;


import com.hsiao.springboot.validate.common.ValidateResult;
import com.hsiao.springboot.validate.group.ValidateGroup;
import com.hsiao.springboot.validate.model.User;
import com.hsiao.springboot.validate.util.ValidationUtil;
import org.junit.Test;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ValidateTest
 * @description: TODO
 * @author xiao
 * @create 2021/9/11
 * @since 1.0.0
 */
public class ValidateTest {

    @Test
    public void test_validate() {
        User user = new User();
        user.setName("ka");
        user.setPassword("密码");
        user.setBirthday("2019.12.1");
        ValidateResult validResult = ValidationUtil.validateBean(user);
        if (validResult.hasErrors()) {
            String properties = validResult.getProperties();
            System.out.println(properties);
        }
    }

    @Test
    public void test6() {
        User user = new User();
        user.setName("ka");
        user.setPassword("密码");
        user.setBirthday("2001.10.02");
        // 指定分组 AccountService.class
        ValidateResult validResult = ValidationUtil.validateBean(user, ValidateGroup.class);
        if (validResult.hasErrors()) {
            String errors = validResult.getErrors();
            String properties = validResult.getProperties();
            System.out.println(errors);
            System.out.println(properties);
        }
    }
}

