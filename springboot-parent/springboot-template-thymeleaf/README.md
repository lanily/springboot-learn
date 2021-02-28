# springboot-template-thymeleaf demo

Thymeleaf 是一种模板语言。那模板语言或模板引擎是什么？常见的模板语言都包含以下几个概念：数据（Data）、模板（Template）、模板引擎（Template Engine）和结果文档（Result Documents）。

数据
数据是信息的表现形式和载体，可以是符号、文字、数字、语音、图像、视频等。数据和信息是不可分离的，数据是信息的表达，信息是数据的内涵。数据本身没有意义，数据只有对实体行为产生影响时才成为信息。
模板
模板，是一个蓝图，即一个与类型无关的类。编译器在使用模板时，会根据模板实参对模板进行实例化，得到一个与类型相关的类。
模板引擎
模板引擎（这里特指用于Web开发的模板引擎）是为了使用户界面与业务数据（内容）分离而产生的，它可以生成特定格式的文档，用于网站的模板引擎就会生成一个标准的HTML文档。
结果文档
一种特定格式的文档，比如用于网站的模板引擎就会生成一个标准的HTML文档。
模板语言用途广泛，常见的用途如下：

页面渲染
文档生成
代码生成
所有 “数据+模板=文本” 的应用场景

> 本 demo 主要演示了 Spring Boot 项目如何集成 thymeleaf 模板引擎

## pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<artifactId>spring-boot-demo-template-thymeleaf</artifactId>
	<version>1.0.0-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>spring-boot-demo-template-thymeleaf</name>
	<description>Demo project for Spring Boot</description>

	<parent>
		<groupId>com.hsiao.springboot</groupId>
		<artifactId>springboot-paret</artifactId>
		<version>1.0.0-SNAPSHOT</version>
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>

		<dependency>
			<groupId>cn.hutool</groupId>
			<artifactId>hutool-all</artifactId>
		</dependency>
	</dependencies>

	<build>
		<finalName>springboot-thymeleaf</finalName>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
```

## IndexController.java

```java
/**
 * <p>
 * 主页
 * </p>
 *
 * @package: com.hsiao.springboot.template.thymeleaf.controller
 * @description: 主页
 * @author: hsiao
 * @date: Created in 2018/10/10 10:12 AM
 * @copyright: Copyright (c) 2018
 * @version: V1.0
 * @modified: hsiao
 */
@Controller
@Slf4j
public class IndexController {

	@GetMapping(value = {"", "/"})
	public ModelAndView index(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();

		User user = (User) request.getSession().getAttribute("user");
		if (ObjectUtil.isNull(user)) {
			mv.setViewName("redirect:/user/login");
		} else {
			mv.setViewName("page/index");
			mv.addObject(user);
		}

		return mv;
	}
}
```

## UserController.java

```java
/**
 * <p>
 * 用户页面
 * </p>
 *
 * @package: com.hsiao.springboot.template.thymeleaf.controller
 * @description: 用户页面
 * @author: hsiao
 * @date: Created in 2018/10/10 10:11 AM
 * @copyright: Copyright (c) 2018
 * @version: V1.0
 * @modified: hsiao
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
	@PostMapping("/login")
	public ModelAndView login(User user, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();

		mv.addObject(user);
		mv.setViewName("redirect:/");

		request.getSession().setAttribute("user", user);
		return mv;
	}

	@GetMapping("/login")
	public ModelAndView login() {
		return new ModelAndView("page/login");
	}
}
```

## index.html

```jsp
<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<header th:replace="~{common/head :: header}"></header>
<body>
<div id="app" style="margin: 20px 20%">
	欢迎登录，<span th:text="${user.name}"></span>！
</div>
</body>
</html>
```

## login.html

```jsp
<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<header th:replace="~{common/head :: header}"></header>
<body>
<div id="app" style="margin: 20px 20%">
   <form action="/demo/user/login" method="post">
      用户名<input type="text" name="name" placeholder="用户名"/>
      密码<input type="password" name="password" placeholder="密码"/>
      <input type="submit" value="登录">
   </form>
</div>
</body>
</html>
```

## application.yml

```yaml
server:
  port: 8080
  servlet:
    context-path: /demo
spring:
  thymeleaf:
    mode: HTML
    encoding: UTF-8
    servlet:
      content-type: text/html
    cache: false
```

## Thymeleaf语法糖学习文档

https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html

