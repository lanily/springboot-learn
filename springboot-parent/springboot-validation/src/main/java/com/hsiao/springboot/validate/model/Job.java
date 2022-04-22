package com.hsiao.springboot.validate.model;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: Route
 * @description: TODO
 * @author xiao
 * @create 2021/9/11
 * @since 1.0.0
 */

import com.hsiao.springboot.validate.group.ValidateGroup;
import com.hsiao.springboot.validate.group.ValidateGroup.Create;
import com.hsiao.springboot.validate.group.ValidateGroup.Update;
import com.hsiao.springboot.validate.validator.DateValidator;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.OptionalInt;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 这个例子主要介绍了分组校验
 * 即在不同的场景下使用不同的校验规则
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job implements Serializable {

    @Max(value = 1000, groups = Create.class)
    @Min(value = 1, groups = Update.class)
    private Long id;

    @NotNull(groups = {Create.class, Update.class})
    @Length(min = 2, max = 10, groups = {Create.class, Update.class})
    private String name;

    @NotNull(groups = {Create.class, Update.class})
    @Length(min = 2, max = 10, groups = {Create.class, Update.class})
    private String position;

    @DecimalMin(value = "0.0", inclusive = false, message = "薪水有点过低啦")
    @DecimalMax(value = "100000.00",inclusive = false, message = "薪水有些超标哦")
    private BigDecimal salary;

    @AssertTrue
    private boolean working;

    /**
     * 数字，正数或0
     */
    @PositiveOrZero
    private transient OptionalInt office;

    @DateValidator(dateFormat = "yyyy-MM-dd", groups = {ValidateGroup.class})
    private String createTime;

    private List<@NotBlank(message = "办公地址不能为空") String> addresses;

}

