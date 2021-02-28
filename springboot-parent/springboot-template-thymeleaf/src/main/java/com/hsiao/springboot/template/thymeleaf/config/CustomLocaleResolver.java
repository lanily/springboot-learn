/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: CustomLocaleResolver Author:   xiao Date:
 * 2020/4/2 9:43 下午 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.template.thymeleaf.config;


import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

/**
 *
 * 自定义语言解析器
 * @projectName springboot-parent
 * @title: CustomLocaleResolver
 * @description: TODO
 * @author xiao
 * @create 2020/4/2
 * @since 1.0.0
 */
public class CustomLocaleResolver implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String l = request.getParameter("l");
        Locale locale = Locale.getDefault();
        if (!StringUtils.isEmpty(l)) {
            String[] split = l.split("_");
            //根据Language、Country 生成 locale
            locale = new Locale(split[0], split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}

