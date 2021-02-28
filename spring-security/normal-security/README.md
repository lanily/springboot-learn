## Spring Security零基础入门之二~验证

### 1. 前言
这是本系列的第二篇文章，上一篇文章主要讲了一个入门例子，全系列传送门如下：

第一篇：[Spring Security零基础入门之一~介绍](https://zhuanlan.zhihu.com/p/47224331)。

第二篇：[Spring Security零基础入门之二~验证](https://zhuanlan.zhihu.com/p/47395352)

第三篇：[Spring Security零基础入门之三~使用数据库进行验证](https://zhuanlan.zhihu.com/p/47584036)

第四篇：[Spring Security零基础入门之四~鉴权](https://zhuanlan.zhihu.com/p/47873694)

Spring Security主要包括两大功能：验证和鉴权。验证就是确认用户的身份，一般采用用户名和密码的形式；鉴权就是确认用户拥有的身份（角色、权限）能否访问受保护的资源。本文主要讲述如何在Spring Security中进行验证功能的开发，阅读本文需要的基础知识：

* 熟练掌握Java
* 掌握Spring Boot基础知识
* 一点点前端知识包括html/css/javascript
* 了解一点后端框架thymeleaf

### 2. 需求以及示例代码
本文是根据第一篇文章进行的改进，在第一篇文章中，实现了以下需求：

- 网站分为首页、登录页、用户页面、管理员页面和报错页面；
- 使用用户名加密码登录，登录错误要报错；
- 不同的用户拥有不同的权限，不同的权限可以访问不同的网页；
- 首页和登录页不需要任何权限；
- 用户页面需要USER权限；
- 管理员页面需要ADMIN权限；
- 如果用户没有登录，则访问需要权限的页面时自动跳转登录页面。

为了让读者掌握具体的验证原理、流程和代码，本文加入了以下三个功能：

1. 修改用户名密码的参数名称
2. 通过自定义一个AuthenticationProvider在系统中加入一个后门
3. 将验证身份信息展示到前端
代码在我的github中：

[apkkids/spring_security_exam](https://link.zhihu.com/?target=https%3A//github.com/apkkids/spring_security_exam)


该项目一共包含四个可独立运行的子项目：本文对应的是normal_security子项目。从Github下载代码后，进入normal_security项目直接运行即可观察效果。

能力强的同学可以直接去看代码了，但还是建议先看下面的原理解释，我将在文章的最后给出代码解释和执行界面展示。

### 3. 原理图
由于Spring Security本身关于验证的架构较为复杂，设计的原理、概念、流程和实现代码很多，因此先画了一个思维导图帮助理解，如下图所示：


![图1 Spring Security原理图](https://pic1.zhimg.com/80/v2-5cea1765ee341da6ab66ea9668f51a3c_hd.jpg)
各部分内容将在后面一一解释。

### 4. 参与验证的要素
Spring Security不仅仅支持网页登录这一种验证模式，它还支持多种其他验证模式。但是其参与验证的要素基本上还是用户、密码、角色、受保护的资源这四种。

用户名称一般在前端由访问者填入，而系统用户在后端一般存储在内存中或数据库中。

密码一般在前端由访问者填入，用于验证用户身份，在`Security 5.0`后密码必须使用`PasswordEncoder`加密，一般来说使用`BCryptPasswordEncoder`即可。

角色，又可以被看做权限，在Spring Security中，有时用Role表示，有时用Authority表示。前端一般看不到系统的角色。后端角色由管理者赋予给用户（可以事先赋予，也可以动态赋予），角色信息一般存储在内存或数据库中。

受保护的资源，一般来说就是指网址，有时也可以将某些函数方法定义为资源，但本文不涉及这类情况。

**参与验证的要素（用户名、密码）在前端由表单提交，由网络传入后端后，会形成一个Authentication类的实例**。（尽管我很讨厌直接介绍各种Class和Interface，但是本文不可避免的要涉及到比较多的类，本文会尽量只介绍最重要的）该实例在进行验证前，携带了用户名、密码等信息；在验证成功后，则携带了身份信息、角色等信息。Authentication接口代码节选如下：

```
public interface Authentication extends Principal, Serializable {
	Collection<? extends GrantedAuthority> getAuthorities();
	Object getCredentials();
	Object getDetails();
	Object getPrincipal();
	boolean isAuthenticated();
	void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;
}
```

其中getCredentials()返回一个Object credentials，它代表验证凭据，即密码；getPrincipal()返回一个Object principal，它代表身份信息，即用户名等；getAuthorities()返回一个Collection<? extends GrantedAuthority>，它代表一组已经分发的权限，即本次验证的角色（本文中权限和角色可以通用）集合。

有了Authentication实例，则验证流程主要围绕这个实例来完成。它会依次穿过整个验证链，并存储在SecurityContextHolder中。也可以像本文中的代码一样，在验证途中伪造一个Authentication实例，骗过验证流程，获得所有权限。

### 5. 验证的规则
验证规则定义了以下几个东西：

1. 受保护的资源即网址，它们一般按访问所需权限分为几类
2. 哪一类资源可以由哪些角色访问
3. 规则定义在WebSecurityConfigurerAdapter的子类中

具体规则的定义方法可以在代码中观察。

### 6. 验证流程
前文已经介绍了Authentication类，它代表了验证信息。

再介绍一个类AuthenticationManager，它是验证管理类的总接口；而具体的验证管理需要ProviderManager类，它具有一个List<AuthenticationProvider> providers属性，这实际上是一个AuthenticationProvider实例构成的验证链。链上都是各种AuthenticationProvider实例，这些实例进行具体的验证工作，它们之间的关系如下图（图来自互联网）所示：


![图2 验证管理类关系图](https://pic3.zhimg.com/80/v2-3523a7b5e03e6af6ce75477e6d0631fa_hd.jpg)
验证成功后，验证实例Authentication会被存入SecurityContextHolder中，而它则利用线程本地存储TLS功能。在验证成功且验证未过期的时间段内，验证会一直有效。而且，可以在需要的地方，从SecurityContextHolder中取出验证信息，并进行操作。例如将验证信息展示在前端。

具体的验证流程如下：

1. 后端从前端的表单得到用户密码，包装成一个Authentication类的对象；
2. 将Authentication对象传给“验证管理器”ProviderManager进行验证；
3. ProviderManager在一条链上依次调用AuthenticationProvider进行验证；
4. 验证成功则返回一个封装了权限信息的Authentication对象（即对象的Collection<? extends GrantedAuthority>属性被赋值）；
5. 将此对象放入安全上下文SecurityContext中；
6. 需要时，可以将Authentication对象从SecurityContextHolder上下文中取出。

注意，在ProviderManager管理的验证链上，任何一个AuthenticationProvider通过了验证，则验证成功。所以，要在系统中留一个后门，只需要在代码中添加一个AuthenticationProvider的子类BackdoorAuthenticationProvider，并在输入特定的用户名（alex）时，直接伪造一个验证成功的Authentication，即可通过验证，代码如下：

```
@Component
public class BackdoorAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        //利用alex用户名登录，不管密码是什么都可以，伪装成admin用户
        if (name.equals("alex")) {
            Collection<GrantedAuthority> authorityCollection = new ArrayList<>();
            authorityCollection.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            authorityCollection.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new UsernamePasswordAuthenticationToken(
                    "admin", password, authorityCollection);
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
```

然后在SecurityConfiguration类中，将BackdoorAuthenticationProvider的实例加入到验证链中即可，代码如下：

```
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    BackdoorAuthenticationProvider backdoorAuthenticationProvider;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       ...省略
        //将自定义验证类注册进去
        auth.authenticationProvider(backdoorAuthenticationProvider);
    }
  ...省略
}
```

### 7. 代码解释
#### 1) 修改用户名密码的参数名称

在前端login.html：

```
        <!-- 1.自定义参数名：myusername和mypassword-->
        <div>
            用户名： <input type="text" name="myusername"/>
        </div>
        <div>
            密码： <input type="password" name="mypassword"/>
        </div>
```
在后端SecurityConfiguration.java：

```
  @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/index","/error").permitAll()
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/user")
                //1.自定义参数名称，与login.html中的参数对应
                .usernameParameter("myusername").passwordParameter("mypassword")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login");
    }
```
#### 2) 通过自定义一个AuthenticationProvider加入一个后门

上一节中已经详细介绍了这个功能的实现。留下后门后，使用用户名alex，配合任何密码，都可以成功登陆并获得管理员权限，而且登陆后的页面上显示的也是admin用户。

3)将验证身份信息展示到前端

前面已经提到，验证成功后，验证信息存入SecurityContextHolder中。因此可以在需要的地方，将其提取出来，然后在前端展示出来。

后端代码：

```
@Controller
public class UserController {

    @RequestMapping("/user")
    public String user(@AuthenticationPrincipal Principal principal, Model model) {
        model.addAttribute("username", principal.getName());

        //从SecurityContextHolder中得到Authentication对象，进而获取权限列表，传到前端
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<GrantedAuthority> authorityCollection = (Collection<GrantedAuthority>) auth.getAuthorities();
        model.addAttribute("authorities", authorityCollection.toString());
        return "user/user";
    }

    @RequestMapping("/admin")
    public String admin(@AuthenticationPrincipal Principal principal, Model model) {
        model.addAttribute("username", principal.getName());

        //从SecurityContextHolder中得到Authentication对象，进而获取权限列表，传到前端
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<GrantedAuthority> authorityCollection = (Collection<GrantedAuthority>) auth.getAuthorities();
        model.addAttribute("authorities", authorityCollection.toString());
        return "admin/admin";
    }
}
```

前端代码：

```
<p>你的当前用户名是：</p>
<p th:text="${username}" style="margin-top: 25px; color: crimson">wxb</p>
<p>你的权限是：</p>
<p th:text="${authorities}" style="margin-top: 25px; color: crimson">authorities</p>
```

### 8. 小结
关于验证的内容实在是比较多，因此文本中依然没有介绍如何利用数据库中存储的信息进行验证，那将会在下一篇文章中介绍。