## Spring Security零基础入门之一~介绍

### 1. 前言
对于绝大部分这篇文章的读者来说，并不关心Spring Security里面那些设计绝妙的n个类，对于那些一上来就给你展示n个类的文章更是深恶痛绝。老板让我明天就交活搞定网站登录验证功能，你还让我看设计模式入门？有鉴于此，我写了这个系列文章，希望没有任何安全、验证、授权管理基础的Coder可以快速入门，在掌握有限知识的前提下，完成网站登录验证等系列功能。

阅读本文需要的基础知识：

* 熟练掌握Java
* 掌握了Spring Boot基础知识
* 一点点前端知识包括html/css/javascript
* 了解一点后端框架thymeleaf

全系列传送门如下：

第一篇：[Spring Security零基础入门之一~介绍](https://zhuanlan.zhihu.com/p/47224331)。

第二篇：[Spring Security零基础入门之二~验证](https://zhuanlan.zhihu.com/p/47395352)

第三篇：[Spring Security零基础入门之三~使用数据库进行验证](https://zhuanlan.zhihu.com/p/47584036)

第四篇：[Spring Security零基础入门之四~鉴权](https://zhuanlan.zhihu.com/p/47873694)

### 2. 需求以及示例代码
即使没有任何网站登录验证功能编码基础的人，也能想出下面的这个功能需求：

1. 网站分为首页、登录页、用户页面、管理员页面和报错页面；
2. 使用用户名加密码登录，登录错误要报错；
3. 不同的用户拥有不同的权限，不同的权限可以访问不同的网页；
4. 首页和登录页不需要任何权限；
5. 用户页面需要USER权限；
6. 管理员页面需要ADMIN权限；
7. 如果用户没有登录，则访问需要权限的页面时自动跳转登录页面。

好，我现在告诉你，实现以上功能，除了前端页面和SpringBoot代码外，和Security相关的代码只需要一个类SecurityConfiguration（继承了WebSecurityConfigurerAdapter），类中只有不到20行代码。

代码在我的github中：

[apkkids/spring_security_exam](https://link.zhihu.com/?target=https%3A//github.com/apkkids/spring_security_exam)


该项目一共包含四个可独立运行的子项目：本文对应的是simple_security子项目。从Github下载代码后，进入simple_security项目直接运行即可观察效果。

能力强的同学可以直接去看代码了，如果有些地方看不懂就继续看下面的文章。

### 3. 代码解释
首先把需求对应的实现一一解释如下：

1. 网站分为首页（index.html）、登录页(login.html)、用户页面(user/user.html)、管理员页面(admin/admin.html)和报错页面(error/403.html)；
2. 使用用户名加密码登录，登录错误要报错(SpringSecurity 自带，不用写代码)；
不同的用户拥有不同的权限，不同的权限可以访问不同的网页（SecurityConfiguration中定义）；
3. 首页和登录页不需要任何权限（SecurityConfiguration中定义）；
4. 用户页面需要USER权限（SecurityConfiguration中定义）；
5. 管理员页面需要ADMIN权限（SecurityConfiguration中定义）；
6. 
如果用户没有登录，则访问需要权限的页面时自动跳转登录页面（SecurityConfiguration中定义）。

解释一下SecurityConfiguration类的第一个方法：

```
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 在内存中创建一个名为 "user" 的用户，密码为 "user"，拥有 "USER" 权限，密码使用BCryptPasswordEncoder加密
         */
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("user").password(new BCryptPasswordEncoder().encode("user")).roles("USER");
        /**
         * 在内存中创建一个名为 "admin" 的用户，密码为 "admin"，拥有 "USER" 和"ADMIN"权限
         */
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin").password(new BCryptPasswordEncoder().encode("admin")).roles("USER","ADMIN");
    }
```

该方法在内存中建立了两个用于验证的User对象，每当用户从网页登录时，与内存中的对象进行比较，如果用户名密码都匹配，则验证通过，否则不通过。值得注意的是，从SpringSecurity5.0以后，这种方式的密码都要使用passwordEncoder进行加密，否则就会报错。

SecurityConfiguration类的第二个方法在代码的注释中已经清楚解释了。
 
### 4. Spring Security原理初探
代码跑起来了，心情舒畅了，再来看看原理，念头就通达了。网络上很多关于Spring Security原理和框架的文章，我只能说他们水平太高，我看不懂，我只好自己写一版。

任何一个权限管理系统，主要都分为两个功能：验证和鉴权。验证就是确认用户的身份，一般采用用户名和密码的形式；鉴权就是确认用户拥有的身份（角色、权限）能否访问受保护的资源。这里面其实就涉及到了三个东西，用户、角色、受保护的资源。在上面的例子中，它们三者如下所示：

用户：`user（角色为USER）`、`admin（角色为USER、ADMIN）`

受保护的资源：user/**（需要USER角色），admin/**（需要ADMIN角色）

那么Spring Security的功能原理如下图所示：

![yuanli](https://pic2.zhimg.com/80/v2-5cf649dc33f7bf6588931cbdbcc0e0d1_hd.jpg)

### 5. 小结
看到这里，你应该有点概念了。但是，这玩意怎么能用啊？我还有一肚子问题呢：

- 用户名、密码和角色不是应该存在数据库里面吗？
- 验证成功后，验证信息保存在哪里？在程序中如何获取这些信息？
- 怎么管理cookie？验证信息多久过期？
- 真正的系统中角色和用户应该能够动态调整，鉴权规则也很复杂，这怎么定制？
- 验证、鉴权的流程是怎么实现的？说好的filter链呢？说好的依赖注入、AOP呢？
这些东西就留待下一篇再解释了，我会给出一个稍微复杂一点的示例来进一步解释Spring Security的原理。