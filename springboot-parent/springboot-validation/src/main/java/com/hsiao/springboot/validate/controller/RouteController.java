package com.hsiao.springboot.validate.controller;


import com.hsiao.springboot.validate.common.R;
import com.hsiao.springboot.validate.group.ValidateGroup.RouteValidEnd;
import com.hsiao.springboot.validate.group.ValidateGroup.RouteValidStart;
import com.hsiao.springboot.validate.util.CommonUtil;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.RouteMatcher.Route;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: RouteController
 * @description: TODO
 * @author xiao
 * @create 2021/9/11
 * @since 1.0.0
 */
@RestController
public class RouteController {

    @RequestMapping("addRoute")
    public R addRoute(@RequestBody @Validated({RouteValidStart.class}) Route route, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errorList = bindingResult.getFieldErrors();
            if (CollectionUtils.isNotEmpty(errorList)) {
                return R.illegalArgument(errorList.get(0).getDefaultMessage());
            }
        }
        // 调用service
        return R.success();
    }

    /**
     * 只校验开始时间和详细地址
     */
    @RequestMapping("addRouteV2")
    public R addRouteV2(@RequestBody Route route) {
        String errorMsg = CommonUtil.getErrorResult(route, RouteValidStart.class);
        if (StringUtils.isNotEmpty(errorMsg)) {
            return R.illegalArgument(errorMsg);
        }
        // 调用service
        return R.success();
    }

    /**
     * 只校验结束时间和详细地址
     */
    @RequestMapping("addRouteV3")
    public R addRouteV3(@RequestBody Route route) {
        String errorMsg = CommonUtil.getErrorResult(route, RouteValidEnd.class);
        if (StringUtils.isNotEmpty(errorMsg)) {
            return R.illegalArgument(errorMsg);
        }
        // 调用service
        return R.success();
    }
}


