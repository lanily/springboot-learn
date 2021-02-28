## Spring Security零基础入门之四~鉴权

### 1. 前言
这是本系列的第四篇文章，前三篇主要讲了使用Spring Security进行验证的原理、流程和例子，全系列传送门如下：

第一篇：[Spring Security零基础入门之一~介绍](https://zhuanlan.zhihu.com/p/47224331)。

第二篇：[Spring Security零基础入门之二~验证](https://zhuanlan.zhihu.com/p/47395352)

第三篇：[Spring Security零基础入门之三~使用数据库进行验证](https://zhuanlan.zhihu.com/p/47584036)

第四篇：[Spring Security零基础入门之四~鉴权](https://zhuanlan.zhihu.com/p/47873694)

本篇的内容主要介绍Spring Security两大功能之二：鉴权。意思是利用数据库中存储的用户角色信息、资源角色信息，对用户访问受限资源的过程进行权限的判断。阅读本文需要的基础知识如下：

* 熟练掌握Java
* 掌握Spring Boot基础知识
* 了解MyBatis的基本使用方法
* 了解MySQL的一些基本使用方法
* 一点点前端知识包括html/css/javascript
* 了解一点后端框架thymeleaf

### 2. 需求以及示例代码
前三章的需求不赘述了，直接把本文完成的需求列举如下：

- 网站分为首页、用户页面、部门1页面、部门2页面、管理员页面和登录页面；
- 使用用户名加密码登录，登录错误要报错；
- 根据前三章的功能，用户来自三个方面：
	- 1）内存用户user、admin；
	- 2）使用后门filter的黑客alex；
	- 3）以及数据库中的用户5个（详见数据库数据）
- 不同的用户拥有不同的权限，不同的权限可以访问不同的网页；
- 登录页不需要任何权限；
- 用户页面需要USER权限；
- 管理员页面需要ADMIN权限；
- 部门1页面需要USER1或者MANAGER权限；
- 部门2页面需要USER2或者MANAGER权限；
- 如果用户没有登录，则访问需要权限的页面时自动跳转登录页面；
- 如果用户已经登录，访问无权页面时服务器会返回一个json，告诉用户访问了没有权限的页面。

代码在我的github中（本项目为auth_security项目）：

[apkkids/spring_security_exam](https://link.zhihu.com/?target=https%3A//github.com/apkkids/spring_security_exam)

运行项目时，apkkids/spring_security_exam运行项目时，

1. 找到spring_security_exam\authorization_withdb\src\main\resources\application.properties，修改mysql的配置；

2. 然后在mysql中运行user_source.sql脚本，创建相应的数据库表。

3. 最后运行springboot应用程序。

### 3. 鉴权原理
前三章已经介绍过，Spring Securtiy的两大功能是验证和鉴权。验证是验明用户的身份，允许用户登录系统。这个验证过程由一系列AuthenticationProvider构成的链来完成。一旦验证成功，用户身份得到确认，它也同时拥有了一些角色，例如ROLE_USER或者ROLE_ADMIN。

而鉴权则是一系列判断用户是否有权限访问资源的过程。以本文为例，我们使用 数据库中的myauthorization.resource表来存储了资源以及所需的角色。例如/depart2/**资源，需要ROLE_ADMIN,ROLE_MANAGER,ROLE_DEPART2这三个角色中的任意一个。而如果登录的用户拥有其中某个资源，则可以顺利访问，否则将会抛出AccessDeniedException异常，进入异常处理程序。

下面按照鉴权处理流程来说一遍鉴权所需编写的代码：

1. 当用户未登录时，访问任何需要权限的资源都会转向登录页面，尝试进行登录；

2. 当用户登录成功时，他会获得一系列角色。

3. 用户访问某资源/xxx时，FilterInvocationSecurityMetadataSource这个类的实现类（本文是MySecurityMetadataSource）会调用getAttributes方法来进行资源匹配。它会读取数据库resource表中的所有记录，对/xxx进行匹配。若匹配成功，则将/xxx对应所需的角色组成一个 Collection<ConfigAttribute>返回；匹配不成功则说明/xxx不需要什么额外的访问权限；

4. 流程来到鉴权的决策类AccessDecisionManager的实现类（MyAccessDecisionManager）中，它的decide方法可以决定当前用户是否能够访问资源。decide方法的参数中可以获得当前用户的验证信息、第3步中获得的资源所需角色信息，对这些角色信息进行匹配即可决定鉴权是否通过。当然，你也可以加入自己独特的判断方法，例如只要用户具有ROLE_ADMIN角色就一律放行；

5. 若鉴权成功则用户顺利访问页面，否则在decide方法中抛出AccessDeniedException异常，这个异常会被AccessDeniedHandler的实现类（MyAccessDeniedHandler）处理。它仅仅是生成了一个json对象，转换为字符串返回给客户端了。

### 4. 代码解释
1. 首先是关于数据库的库表设计问题，本文中依然使用了单表结构，角色这个字段在两个表中是以逗号分隔，然后在程序中分解开来的，如下所示：

```
-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `address` varchar(64) DEFAULT NULL COMMENT '联系地址',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
  `roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('1', 'Adam', 'beijing', 'adam','$2a$10$9SIFu8l8asZUKxtwqrJM5ujhWarz/PMnTX44wXNsBHfpJMakWw3M6', 'ROLE_USER');
INSERT INTO `user` VALUES ('2', 'SuperMan', 'shanghang', 'super','$2a$10$9SIFu8l8asZUKxtwqrJM5ujhWarz/PMnTX44wXNsBHfpJMakWw3M6', 'ROLE_USER,ROLE_ADMIN');
INSERT INTO `user` VALUES ('3', 'Manager', 'beijing', 'manager','$2a$10$9SIFu8l8asZUKxtwqrJM5ujhWarz/PMnTX44wXNsBHfpJMakWw3M6', 'ROLE_USER,ROLE_MANAGER');
INSERT INTO `user` VALUES ('4', 'User1', 'shanghang', 'user1','$2a$10$9SIFu8l8asZUKxtwqrJM5ujhWarz/PMnTX44wXNsBHfpJMakWw3M6', 'ROLE_USER,ROLE_DEPART1');
INSERT INTO `user` VALUES ('5', 'User2', 'shanghang', 'user2','$2a$10$9SIFu8l8asZUKxtwqrJM5ujhWarz/PMnTX44wXNsBHfpJMakWw3M6', 'ROLE_USER,ROLE_DEPART2');
COMMIT;

-- ----------------------------
--  Table structure for `resource`
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL COMMENT '资源',
  `roles` varchar(255) DEFAULT NULL COMMENT '所需角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
-- ----------------------------
--  Records of `resource`
-- ----------------------------
BEGIN;
INSERT INTO `resource` VALUES ('1', '/depart1/**', 'ROLE_ADMIN,ROLE_MANAGER,ROLE_DEPART1');
INSERT INTO `resource` VALUES ('2', '/depart2/**', 'ROLE_ADMIN,ROLE_MANAGER,ROLE_DEPART2');
INSERT INTO `resource` VALUES ('3', '/user/**', 'ROLE_ADMIN,ROLE_USER');
INSERT INTO `resource` VALUES ('4', '/admin/**', 'ROLE_ADMIN');
COMMIT;
```

而在实际项目中，应该使用多表关联更加灵活。

2. 关于FilterInvocationSecurityMetadataSource类（安全元数据源类）如何设置到WebSecurityConfigurerAdapter中的问题，综合网上目前的文章，使用JavaConfig只有一种方法，即使用withObjectPostProcessor方法，代码如下：

```
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    BackdoorAuthenticationProvider backdoorAuthenticationProvider;
    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    MyAccessDecisionManager myAccessDecisionManager;
    @Autowired
    MySecurityMetadataSource mySecurityMetadataSource;
    @Autowired
    MyAccessDeniedHandler myAccessDeniedHandler;

...省略
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(mySecurityMetadataSource);
                        object.setAccessDecisionManager(myAccessDecisionManager);
                        return object;
                    }
                })
   ...省略  
```        
   
3. 鉴权决策类AccessDecisionManager设置到WebSecurityConfigurerAdapter中有两种方法，其一是如上的方法，其二是

        `http
                .authorizeRequests().accessDecisionManager(myAccessDecisionManager)`
4. 本文中MyAccessDeniedHandler类只是返回一个json字符串。如果不处理AccessDeniedException异常，则会显示403错误页面。此处返回的字符串可以和前端结合展示更加丰富的页面。

### 5. 小结
在网上其他介绍鉴权的文章中，经常提到Spring Security内置了三个基于投票的AccessDecisionManager实现类，它们分别是AffirmativeBased、ConsensusBased和UnanimousBased。然后如何实现一个投票类。但是我觉得直接继承AccessDecisionManager，然后重写decide方法更加简便。