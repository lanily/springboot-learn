package com.hsiao.springboot.validate.validator;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ValueOfEnumValidator
 * @description: TODO
 * @author xiao
 * @create 2021/9/12
 * @since 1.0.0
 */

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements
        ConstraintValidator<ValueOfEnum, CharSequence> {

    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass()
                .getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value.toString());
    }
}

