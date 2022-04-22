/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: User Author: xiao Date: 2020/3/29 7:02 下午 History:
 * <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.validate.model;

import com.hsiao.springboot.validate.validator.IdentityCardNumber;
import com.hsiao.springboot.validate.validator.ValueOfEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * ======================= 值校验 ========================
 * @Null 被注解的元素必须为null
 * @NotNull 被注解的元素必须不为null
 * @NotBlank 验证注解的元素值不为空（不为null、去除首位空格后长度为0） ，并且类型为String。
 * @NotEmpty 验证注解的元素值不为null且不为空（字符串长度不为0、集合大小不为0） ，并且类型为String。
 * @AssertFalse 被注解的元素必须为false，并且类型为boolean。
 * @AssertTrue 被注解的元素必须为true，并且类型为boolean。
 *
 * ======================= 范围校验 ========================
 * @Min(value) 被注解的元素其值必须大于等于给定的值，并且类型为int，long，float，double。
 * @Max(value) 被注解的元素其值必须小于等于给定的值，并且类型为int，long，float，double。
 * @DecimalMin(value) 验证注解的元素值大于等于@DecimalMin指定的value值，并且类型为BigDecimal。
 * @DecimalMax(value) 验证注解的元素值小于等于@DecimalMax指定的value值 ，并且类型为BigDecimal。
 * @Range(min=, max=)  验证注解的元素值在最小值和最大值之间，并且类型为BigDecimal，BigInteger，CharSequence，byte，short，int，long。
 * @Past 被注解的元素必须为过去的一个时间，并且类型为java.util.Date。
 * @Future 被注解的元素必须为未来的一个时间，并且类型为java.util.Date。
 *
 * ======================= 长度校验 ========================
 * @Size(min=, max=)   被注解的元素的长度必须在指定范围内，并且类型为String，Array，List，Map。
 * @Length(min=, max=) 验证注解的元素值长度在min和max区间内 ，并且类型为String。
 *
 * ======================= 格式校验 ========================
 * @Digits(integer, fraction)  验证注解的元素值的整数位数和小数位数上限 ，并且类型为float，double，BigDecimal。
 * @Pattern(value) 被注解的元素必须符合指定的正则表达式，并且类型为String。
 * @Email 验证注解的元素值是Email，也可以通过regexp和flag指定自定义的email格式，类型为String。
 *
 *
 * @URL(protocol=, host=, port=, regexp=, flags=)   被注释的字符串必须是一个有效的url
 * @CreditCardNumber 被注释的字符串必须通过Luhn校验算法，银行卡，信用卡等号码一般都用Luhn计算合法性
 *
 * 嵌套验证
 * @Valid递归的对关联的对象进行校验
 *
 * 用户实体类
 *
 * @projectName springboot-parent
 * @title: User
 * @description: TODO
 * @author xiao
 * @create 2020/3/29
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements Serializable {

    /** 编号 */
    @Id
    @GeneratedValue
    private Long id;

    /** 名称 */
    /**
     * @NotEmpty注解
     * 验证注解的元素值不为null且不为空（字符串长度不为0、集合大小不为0） ，并且类型为String。
     */
    @NotEmpty(message = "姓名不能为空")
    @Size(min = 2, max = 8, message = "姓名长度必须大于 2 且小于 20 字")
//    @Length(max = 20)
    /** 用户名*/
//    @NotBlank(message = "用户名不能为空")
//    @Length(max = 20, message = "用户名不能超过20个字符")
//    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9\\*]*$", message = "用户昵称限制：最多20字符，包含文字、字母和数字")
    private String name;

    /** 密码 */
    @NotNull
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z~!@#$%^*]+$)[0-9A-Za-z~!@#$%^*]{8,20}$", message = "密码格式不正确，请输入8-20位的密码，必须包含数字和字母，支持特殊符号~!@#$%^*")
    private String password;

    /** 年龄 */
    @NotNull(message = "年龄不能为空")
//    @Min(value = 1, message = "年龄大于 0")
//    @Max(value = 150, message = "年龄不大于 150")
    @Range(min = 1, max = 150)
//    @Max(10)
//    @Min(1)
//    @Positive
    private Integer age;

    /** 性别 */
    @ValueOfEnum(enumClass = SEX.class)
    private String sex;

    /** 身高 */
    @Digits(integer = 3, fraction = 2, message = "整数位上限为3位，小数位上限为2位")
    private BigDecimal height;

//    @Pattern(regexp="^(\\d{18,18}|\\d{15,15}|(\\d{17,17}[x|X]))$", message="身份证格式错误")
    /** 自定义校验身份证 */
    @IdentityCardNumber(message = "id card 不合法")
    private String idCard;

    /** 出生时间 */
    @NotEmpty(message = "出生时间不能为空")
    private String birthday;

    /** 手机号 */
    @NotBlank(message = "手机号不能为空")
//  @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    @Pattern(regexp = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", message = "手机号码格式错误")
    private String phone;

    /** 邮箱 */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
//    @Pattern(regexp = "^(.+)@(.+)$",message = "邮箱的格式不合法")
    private String email;

    /*** 加入时间 */
    @DateTimeFormat(pattern = "yyy-MM-dd")
    @Past(message = "必须为过去的时间")
    private Date joinTime;

    /*** 离职时间 */
    @DateTimeFormat(pattern = "yyy-MM-dd")
    @Future(message = "必须为未来的时间")
    private Date resignTime;

    /** 岗位 **/
    @NotNull(message = "工作岗位不能为空")
    @Valid
    private Job job;


    public User(String name, Integer age, String birthday) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }
}
