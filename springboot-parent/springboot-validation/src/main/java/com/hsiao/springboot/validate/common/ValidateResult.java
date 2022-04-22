package com.hsiao.springboot.validate.common;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ValidateResult
 * @description: TODO
 * @author xiao
 * @create 2021/9/11
 * @since 1.0.0
 */

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

/**
 * 校验结果类
 */
@Data
public class ValidateResult {

    /**
     * 是否有错误
     */
    private boolean hasErrors;

    /**
     * 错误信息
     */
    private List<ErrorMessage> errors;

    public ValidateResult() {
        this.errors = new ArrayList<>();
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    /**
     * 获取所有验证信息
     *
     * @return 集合形式
     */
    public List<ErrorMessage> getAllErrors() {
        return errors;
    }

    /**
     * 获取所有验证信息
     *
     * @return 字符串形式
     */
    public String getErrors() {
        StringBuilder sb = new StringBuilder();
        for (ErrorMessage error : errors) {
            sb.append(error.getPropertyPath()).append(":").append(error.getMessage())
                    .append(" ");
        }
        return sb.toString();
    }

    public void addError(String propertyName, String message) {
        this.errors.add(new ErrorMessage(propertyName, message));
    }

    /**
     * 获取去重之后的非法属性值，以逗号分隔
     * @return
     */
    public String getProperties() {
        return errors.stream().map(error -> error.getPropertyPath()).collect(Collectors.toSet())
                .stream().collect(Collectors.joining(", "));
    }
}

