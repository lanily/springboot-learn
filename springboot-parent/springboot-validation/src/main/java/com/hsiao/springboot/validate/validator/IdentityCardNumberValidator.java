package com.hsiao.springboot.validate.validator;


import com.hsiao.springboot.validate.util.IdCardValidatorUtils;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: IdentityCardNumberValidator
 * @description: TODO
 * @author xiao
 * @create 2021/9/11
 * @since 1.0.0
 */
public class IdentityCardNumberValidator implements
        ConstraintValidator<IdentityCardNumber, Object> {

    @Override
    public void initialize(IdentityCardNumber identityCardNumber) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return IdCardValidatorUtils.isValidate18Idcard(o.toString());
    }
}

