## Spring Security零基础入门之三~使用数据库进行验证

### 1. 前言
这是本系列的第三篇文章，前两篇主要讲了使用Spring Security进行验证的原理、流程和一个比较简单的例子。全系列传送门如下：

第一篇：[Spring Security零基础入门之一~介绍](https://zhuanlan.zhihu.com/p/47224331)。

第二篇：[Spring Security零基础入门之二~验证](https://zhuanlan.zhihu.com/p/47395352)

第三篇：[Spring Security零基础入门之三~使用数据库进行验证](https://zhuanlan.zhihu.com/p/47584036)

第四篇：[Spring Security零基础入门之四~鉴权](https://zhuanlan.zhihu.com/p/47873694)

这一篇的内容主要是介绍如何利用数据库中存储的信息进行验证。Spring Security内置了一些类，利用这些类可以很方便的将数据库中存储的用户、密码、角色信息导入系统中进行验证。由于涉及到数据库了，因此阅读本文需要的基础知识比前两章要多一些：

* 熟练掌握Java
* 掌握Spring Boot基础知识
* 了解MyBatis的基本使用方法
* 了解MySQL的一些基本使用方法
* 一点点前端知识包括html/css/javascript
* 了解一点后端框架thymeleaf

### 2. 需求以及示例代码

第一篇文章中，实现了以下需求：

- 网站分为首页、登录页、用户页面、管理员页面和报错页面；
- 使用用户名加密码登录，登录错误要报错；
- 不同的用户拥有不同的权限，不同的权限可以访问不同的网页；
- 首页和登录页不需要任何权限；
- 用户页面需要USER权限；
- 管理员页面需要ADMIN权限；
- 如果用户没有登录，则访问需要权限的页面时自动跳转登录页面。

第二篇中加入了以下三个功能：

+ 修改用户名密码的参数名称
+ 通过自定义一个AuthenticationProvider在系统中加入一个后门
+ 将验证身份信息展示到前端

本文将加入以下功能：

- 从数据库中读取用户名、密码，与前端输入的信息进行对比验证；
- 验证通过后，登陆用户会得到数据库中存储的角色信息。

代码在我的github中（本项目为withdb-security项目）：

[apkkids/spring_security_exam](https://link.zhihu.com/?target=https%3A//github.com/apkkids/spring_security_exam)
​

### 3. 原理介绍
在第二章中介绍了验证的流程，因此可知，要加入想自定义的验证功能，就是向ProviderManager中加入一个自定义的AuthenticationProvider实例。为了加入使用数据库进行验证的DaoAuthenticationProvider类（这个类在我们的代码中是透明的）实例，可以使用AuthenticationManagerBuilder类的userDetailsService(UserDetailsService)方法。代码如下：

```
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        ...省略
        //加入数据库验证类，下面的语句实际上在验证链中加入了一个DaoAuthenticationProvider
        auth.userDetailsService(myUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
```

而我们需要掌握的，就是由Security框架提供的两个接口UserDetails和UserDetailsService。其中UserDetails接口中定义了用于验证的“用户详细信息”所需的方法。而UserDetailsService接口仅定义了一个方法loadUserByUsername(String username) 。这个方法由接口的实现类来具体实现，它的作用就是通过用户名username从数据库中查询，并将结果赋值给一个UserDetails的实现类实例。验证流程如下：

由于在上面的configure方法中调用了userDetailsService(myUserDetailsService)方法，因此在ProviderManager的验证链中加入了一个DaoAuthenticationProvider类的实例；
验证流程进行到DaoAuthenticationProvider时，它调用用户自定义的myUserDetailsService服务的loadUserByUsername方法，这个方法会从数据库中查询用户名是否存在；
若存在，则从数据库中返回的信息会组成一个UserDetails接口的实现类的实例，并将此实例返回给DaoAuthenticationProvider进行密码比对，比对成功则通过验证。
仍然要注意，在ProviderManager管理的验证链上，任何一个AuthenticationProvider通过了验证，则验证成功。第二章加入的验证方法依然存在，所以系统user、admin、alex等用户依然能通过验证。

### 4. 代码解释

1. 要实现数据库验证，第一步是设计mysql数据库的库表结构，为了清晰起见，本文只使用了一个表，相关sql语句在项目的user.sql文件中：

```
CREATE DATABASE `mysecurity` DEFAULT CHARACTER SET utf8;

USE `mysecurity`;
SET FOREIGN_KEY_CHECKS=0;
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
  `roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '身份',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('1', 'Adam', 'beijing', 'adam','$2a$10$9SIFu8l8asZUKxtwqrJM5ujhWarz/PMnTX44wXNsBHfpJMakWw3M6', 'ROLE_USER');
INSERT INTO `user` VALUES ('2', 'SuperMan', 'shanghang', 'super','$2a$10$9SIFu8l8asZUKxtwqrJM5ujhWarz/PMnTX44wXNsBHfpJMakWw3M6', 'ROLE_USER,ROLE_ADMIN');
COMMIT;
运行此sql文件，就会建立一个user表，并插入两条记录，注意记录中的密码字段已经用BCryptPasswordEncoder加过密了，就是pwd加密后的字符串。
```

2. 接下来就是实现`UserDetails`接口，User类中唯一值得注意的就是getAuthorities方法的实现，它将数据库中的roles字段取出来分解为多个SimpleGrantedAuthority对象加入List中。

3. 接下来用MyBatis实现User的mapper接口，接口中仅定义了selectByUsername方法。

4. 用UserDetailsServiceImpl实现UserDetailsService接口，使用UserMapper来进行数据查询，并实现UserDetailsService的loadUserByUsername方法即可。

5. 在SecurityConfiguration类中调用`auth.userDetailsService(userDetailsService)`方法，在验证链中加入一个DaoAuthenticationProvider。

如此就实现了使用数据库进行Spring Security的验证，你可以试试在数据库中加入新的记录，并使用新加入的用户登录。

### 5. 小结
使用数据库进行验证其实只需要掌握两个接口即可，即UserDetailsService和UserDetails。除此外还要设计好自己的数据库表格。本文中为了简单，把用户名、密码和角色存储在一张表中，而实际上应该将用户名-密码和角色分开存储，便于实现动态的权限管理。这部分内容我们将会在下一篇文章中详细介绍。

另，Spring Security实际上提供了一套默认的数据库表格和具体的实现类，如果觉得自己的系统在验证功能上没有特殊性，也可以直接使用它的库表结构和实现类。