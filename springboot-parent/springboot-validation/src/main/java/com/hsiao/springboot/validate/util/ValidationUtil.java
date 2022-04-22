package com.hsiao.springboot.validate.util;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ValidationUtil
 * @description: TODO
 * @author xiao
 * @create 2021/9/11
 * @since 1.0.0
 */

import com.hsiao.springboot.validate.common.ValidateResult;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.hibernate.validator.HibernateValidator;

public class ValidationUtil {

    /**
     * 开启快速结束模式 failFast (true)
     */
    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure()
            .failFast(false).buildValidatorFactory().getValidator();

    /**
     * 校验对象
     *
     * @param t      bean
     * @param groups 校验组
     * @return ValidResult
     */
    public static <T> ValidateResult validateBean(T t, Class<?>... groups) {
        ValidateResult result = new ValidateResult();
        Set<ConstraintViolation<T>> violationSet = validator.validate(t, groups);
        boolean hasError = violationSet != null && violationSet.size() > 0;
        result.setHasErrors(hasError);
        if (hasError) {
            for (ConstraintViolation<T> violation : violationSet) {
                result.addError(violation.getPropertyPath().toString(), violation.getMessage());
            }
        }
        return result;
    }

    /**
     * 校验bean的某一个属性
     *
     * @param obj          bean
     * @param propertyName 属性名称
     * @return ValidResult
     */
    public static <T> ValidateResult validateProperty(T obj, String propertyName) {
        ValidateResult result = new ValidateResult();
        Set<ConstraintViolation<T>> violationSet = validator.validateProperty(obj, propertyName);
        boolean hasError = violationSet != null && violationSet.size() > 0;
        result.setHasErrors(hasError);
        if (hasError) {
            for (ConstraintViolation<T> violation : violationSet) {
                result.addError(propertyName, violation.getMessage());
            }
        }
        return result;
    }
}

