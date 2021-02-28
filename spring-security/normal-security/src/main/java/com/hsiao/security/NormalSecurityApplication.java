package com.hsiao.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 需求以及示例代码
 * 实现了以下需求：
 *
 * 网站分为首页、登录页、用户页面、管理员页面和报错页面；
 * 使用用户名加密码登录，登录错误要报错；
 * 不同的用户拥有不同的权限，不同的权限可以访问不同的网页；
 * 首页和登录页不需要任何权限；
 * 用户页面需要USER权限；
 * 管理员页面需要ADMIN权限；
 * 如果用户没有登录，则访问需要权限的页面时自动跳转登录页面。
 * 为了让读者掌握具体的验证原理、流程和代码，本文加入了以下三个功能：
 *
 * 修改用户名密码的参数名称
 * 通过自定义一个AuthenticationProvider在系统中加入一个后门
 * 将验证身份信息展示到前端
 */
@SpringBootApplication
public class NormalSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(NormalSecurityApplication.class, args);
	}
}
