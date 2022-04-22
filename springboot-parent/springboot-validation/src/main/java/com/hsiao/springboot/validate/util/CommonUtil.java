package com.hsiao.springboot.validate.util;


import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: CommonUtil
 * @description: TODO
 * @author xiao
 * @create 2021/9/11
 * @since 1.0.0
 */
public class CommonUtil {

    public static String getErrorResult(Object o, Class<?>... groups) {
        StringBuilder errorMsg = new StringBuilder();
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<Object>> set = validator.validate(o, groups);
        for (ConstraintViolation<Object> constraintViolation : set) {
            errorMsg.append(constraintViolation.getMessage()).append(" ");
        }
        return errorMsg.toString();
    }
}

