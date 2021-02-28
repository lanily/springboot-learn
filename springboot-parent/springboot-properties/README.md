在开发`Spring Boot`应用的时候，也会遇到使用外部配置资源，这些配置资源能够与代码相互配合，避免硬编码方式，提供应用数据或行为变化的灵活性。也就是说我们需要灵活的用好配置文件，接下来就来介绍获取配置文件的内容的各种姿势。

![spring-boot-properties](https://images2018.cnblogs.com/blog/806956/201804/806956-20180401220714608-87698899.png)
### 支持多种外部配置方式

Spring Boot 可以将配置外部化，即分离存储在 classpath 之外，这种模式叫做 “外化配置”。常用在不同环境中，将配置从代码中分离外置，只要简单地修改下外化配置，可以依旧运行相同的应用代码。外化配置表现形式不单单是 .properties 和 .yml 属性文件，还可以使用环境变量和命令行参数等来实现。那么，多处配置了相同属性时，Spring Boot 是通过什么方式来控制外化配置的冲突呢？答案是外化配置优先级。

1、常见的外部配置资源

在开发的过程中，也许会用到不同的配置资源，这里简单的列一下常见的：

* Properties 文件
* YAML 文件
* XML 文件
* 环境变量
* Java 系统属性
* 命令行参数配置

2、资源的加载顺序

Spring Boot规定了这些外部配置资源的加载的优先级，详情参考官方文档[Externalized Configuration](https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/#boot-features-external-config)小节。这里简要说明一下：

![http://static.zybuluo.com/zhuanxu/sv03n9gm6s0ak1uhfaxxctvp/image_1c7aif46leqh1h2t95luec9u19.png](http://static.zybuluo.com/zhuanxu/sv03n9gm6s0ak1uhfaxxctvp/image_1c7aif46leqh1h2t95luec9u19.png)

1. 本地 `Devtools` 全局配置,热加载，也就是根目录下的开发工具全局设置属性(当开发工具激活时为`~/.spring-boot-devtools.properties`)。
2. 测试配置，具体指@TestPropertySource注解和@SpringBootTest#properties注解属性值。
3. 命令行配置,即命令行参数
4. `SPRINGAPPLICATIONJSON` 配置
5. Servlet 参数：`ServletConfig` 初始化参数配置 和 `ServletContext` 初始化参数配置
6. `Java` 环境的 `JNDI` 参数配置
7. `Java` 系统的属性配置 `System.getProperties()`。
8. `OS` 环境变量配置
9. 只能随机属性的 `RandomValuePropertySource` 配置
10. 工程 `jar` 之外的多环境配置文件（`application- {profile}.properties` 或 `YAML`）
11. 工程 `jar` 之内的多环境配置文件（`application- {profile}.properties` 或 `YAML`）
12. 工程 `jar` 之外的应用配置文件（`application.properties` 或 `YAML`）
13. 工程 `jar` 之内的应用配置文件（`application.properties` 或 `YAML`）
14. `@Configuration` 类中的 `@PropertySource` 注解配置
15. 默认属性配置（`SpringApplication.setDefaultProperties` 指定）

### 通过命令行设置属性值

相信使用过一段时间`Spring Boot`的用户，一定知道这条命令：`java -jar app.jar --server.port=8888`，通过使用`—server.port`属性来设置`xxx.jar`应用的端口为`8888`。

在命令行运行时，连续的两个减号--就是对`application.properties`中的属性值进行赋值的标识。所以，`java -jar app.jar --server.port=8888`命令，等价于我们在`application.properties`中添加属性`server.port=8888`，该设置在样例工程中可见，读者可通过删除该值或使用命令行来设置该值来验证。

通过命令行来修改属性值固然提供了不错的便利性，但是通过命令行就能更改应用运行的参数，那岂不是很不安全？是的，所以`Spring Boot`也贴心的提供了屏蔽命令行访问属性的设置，只需要这句设置就能屏蔽：`SpringApplication.setAddCommandLineProperties(false)`。

**注意**：命令行参数在app.jar的后面！

### Java系统属性
注意Java系统属性位置`java -Dname="springboot" -jar app.jar`，可以配置的属性都是一样的，优先级不同。

例如`java -Dname="springboot" -jar app.jar --name="Spring!"`中`name`值为`Spring`!

### 操作系统环境变量
配置过JAVA_HOME的应该都了解这一个。
这里需要注意的地方，有些OS可以不支持使用.这种名字，如`server.port`，这种情况可以使用`SERVER_PORT`来配置。

### 应用配置文件（.properties或.yml）

在 Spring Boot 中，配置文件有两种不同的格式，一个是 `properties` ，另一个是 `yaml` 。

虽然 `properties` 文件比较常见，但是相对于 `properties` 而言，`yaml` 更加简洁明了，而且使用的场景也更多，很多开源项目都是使用 `yaml` 进行配置（例如 `Hexo`）。除了简洁，`yaml` 还有另外一个特点，就是 `yaml` 中的数据是有序的，`properties` 中的数据是无序的，在一些需要路径匹配的配置中，顺序就显得尤为重要（例如我们在 `Spring Cloud Zuul` 中的配置），此时我们一般采用 `yaml`。

在配置文件中直接写：

```
name=springboot
server.port=8080
```

.yml格式的配置文件如：

```
name: springboot
server:
port: 8080
```

当有前缀的情况下，使用`.yml`格式的配置文件更简单。关于.yml配置文件用法请看这里(`YAML` 文件)

**注意**：使用`.yml`时，属性名的值和冒号中间必须有空格，如`name: springboot`，`name:springboot`就是错的。

#### 位置问题
首先，当我们创建一个 Spring Boot 工程时，默认 resources 目录下就有一个 application.properties 文件，可以在 application.properties 文件中进行项目配置，但是这个文件并非唯一的配置文件，在 Spring Boot 中，一共有 4 个地方可以存放 application.properties 文件。

1. 当前项目根目录下的 config 目录下
2. 当前项目的根目录下
3. resources 目录下的 config 目录下
4. resources 目录下

按如上顺序，四个配置文件的优先级依次降低。


```

${basedir} -- project
|-- pom.xml
|-- application.properties                  ②
|-- config
|   |
|   `-- application.properties              ①
|-- src
|   |-- main
|   |   `-- java
|   |   `-- filters
|   |   `-- resources
|   |       `-- application.properties      ④
|   |       |
|   |       `-- config
|   |           |
|   |           `-- application.properties  ③
|   `-- test
|   |   `-- java
|   |   `-- resources
|   |   `-- filters
|   `-- it
|   `-- assembly
|   `-- site
`-- LICENSE.txt
`-- NOTICE.txt
`-- README.txt

```

#### `YAML` 文件
在 Spring Boot 中，官方推荐使用 properties 或者 `YAML` 文件来完成配置。

>YAML（英语发音：/ˈjæməl/，尾音类似camel骆驼）是一个可读性高，用来表达资料序列的格式。YAML参考了其他多种语言，包括：C语言、Python、Perl，并从XML、电子邮件的数据格式（RFC 2822）中获得灵感。Clark Evans在2001年首次发表了这种语言，另外Ingy döt Net与Oren Ben-Kiki也是这语言的共同设计者。目前已经有数种编程语言或脚本语言支援（或者说解析）这种语言。YAML是”YAML Ain’t a Markup Language”（YAML不是一种标记语言）的递回缩写。在开发的这种语言时，YAML 的意思其实是：”Yet Another Markup Language”（仍是一种标记语言），但为了强调这种语言以数据做为中心，而不是以标记语言为重点，而用反向缩略语重新命名。AML的语法和其他高阶语言类似，并且可以简单表达清单、散列表，标量等资料形态。它使用空白符号缩排和大量依赖外观的特色，特别适合用来表达或编辑数据结构、各种设定档、倾印除错内容、文件大纲（例如：许多电子邮件标题格式和YAML非常接近）。尽管它比较适合用来表达阶层式（hierarchical model）的数据结构，不过也有精致的语法可以表示关联性（relational model）的资料。由于YAML使用空白字元和分行来分隔资料，使得它特别适合用grep／Python／Perl／Ruby操作。其让人最容易上手的特色是巧妙避开各种封闭符号，如：引号、各种括号等，这些符号在巢状结构时会变得复杂而难以辨认。 —— 维基百科


##### `YAML` 语法规则：

* 大小写敏感
* 缩进表示层级
* 缩进只能使用空格
* 空格的数量不重要，但是相同层级的元素要左侧对齐
* `#` 开头的行表示注释

##### `YAML` 支持的数据结构：

1. 单纯的变量，不可再分的单个的值，如数字，字符串等。

```
name: Darcy
age: 12
# ~表示NULL值
email: ~ 
# 多行字符串可以使用|保留换行符，也可以使用>折叠换行。
# +表示保留文字块末尾的换行，-表示删除字符串末尾的换行。
message:|-
  Hello world
```

2. 数组，一组按次序排列的值。

```
lang:
 - java
 - golang
 - c
# 或者行内写法
lang:[java,golang,c]
```

3. 对象，键值对的集合。

```
person:
  name:Darcy
  age:20
# 或者行内写法
person:{name:Darcy,age:20}
```

使用 `YAML` 支持的三种数据结构通过组合可以形成复杂的复合结构。

```
# 服务启动端口号
server:
  port: 8080
# 配置person属性值
person:
  last-name: Darcy
  age: 20
  birth: 2018/01/01
  email: gmail@gmail.com
  maps:
    key1:java
    key2:golang
  lists:
  - a
  - b
  - c
  dog:
    name: 旺财
    age: 2
```

**注意**：`YAML`目前还有一些不足，`YAML`文件无法通过`@PropertySource`注解来加载配置。但是，`YAML`加载属性到内存中保存的时候是有序的，所以当配置文件中的信息需要具备顺序含义时，`YAML`的配置方式比起`properties`配置文件更有优势。

#### `Properties` 文件
`properties` 配置文件简单好用，在各种配置环境里都可以看到它的身影，它简单易用，但是在配置复杂结构时不如`YAML` 优雅美观。同样拿上面的 `YAML` 的复合结构举例，演示同样的配置在 `properties`文件中的写法。

```
# 服务启动端口号
server.port=8080
# 配置属性值（使用IDE进行配置需要处理编码问题，不然中文会发送乱码现象）
person.last-name=张三
person.age=18
person.birth=2018/12/06
person.email=niu@gmail.com
person.maps.key1=c
person.maps.key2=java
person.maps.key3=golang
person.lists=a,b,c,d
person.list[0]=1
person.list[1]=2
person.list[2]=3
person.dog.name=旺财
person.dog.age=1
```

#### 随机数与占位符
`RandomValuePropertySource` 类对于注入随机值很有用（例如，注入秘密或测试用例）。它可以生成整数，长整数，`uuid` 或字符串等，通过 `Spring Boot` 对我们的封装，我们可以轻松的使用。

占位符允许在配置的值中引用之前定义过的变量。

```
# 生成随机值
bootapp.secret=$ {random.value}
bootapp.number=$ {random.int}
bootapp.bignumber=$ {random.long}
bootapp.uuid=$ {random.uuid}
bootapp.number.less.than.ten=$ {random.int（10）}
bootapp.number.in.range=$ {random.int [1024,65536]}
# 属性的占位符
bootapp.name=SpringBoot
bootapp.description=${bootapp.name}是一个spring应用程序
```

#### 参数间的引用

在`application.properties`中的各个参数之间也可以直接引用来使用，就像下面的设置：

```
blog.author=hsiao
blog.title=Spring Boot
blog.desc=${blog.author}正在学习《${blog.title}》
```

### 配置自动提示

在配置自定义属性时，如果想要获得和配置Spring Boot属性自动提示一样的功能，则需要加入下面的依赖：
若是依旧无法自动提示，可以尝试开启IDE的Annonation Processing

```
<dependencies>
       <!-- 导入配置文件处理器，在配置相关文件时候会有提示 -->
       <!-- 支持 @ConfigurationProperties 注解 --> 
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-configuration-processor</artifactId>
           <optional>true</optional>
       </dependency>

   </dependencies>
```

#### 配置属性校验#
自定义配置文件时，可以使用 @Validated 注解对注入的值进行一些简单的校验，示例代码:

```
@Validated
@Configuration
@ConfigurationProperties(prefix = "person")
public class Person {
    @Email
    private String mail;

    public String getMail() {
        return mail;
    }
  
    public void setMail(String mail) {
        this.mail = mail;
    }
}
```
@Email 注解会对mail字段的注入值进行检验，如果注入的不是一个合法的邮件地址则会抛出异常。

其它常见注解：

* @AssertFalse 校验false
* @AssertTrue 校验true
* @DecimalMax(value=,inclusive=) 小于等于value，inclusive=true，是小于等于
* @DecimalMin(value=,inclusive=) 与上类似
* @Max(value=) 小于等于value
* @Min(value=) 大于等于value
* @NotNull 检查Null
* @Past 检查日期
* @Pattern(regex=,flag=) 正则
* @Size(min=, max=) 字符串，集合，map限制大小
* @Validate 对po实体类进行校验

上述的这些注解位于 javax.validation.constraints 包下，具体用法查看注释即可了解。

#### 自定义配置文件
除了在默认的application文件进行属性配置，我们也可以自定义配置文件，例如新建 `peoson.properties` ，配置内容如下:

`person.mail=yster@foxmail.com`

然后在配置类中使用`@PropertySource`注解注入该配置文件

```
@Configuration
@ConfigurationProperties(prefix = "person")
@PropertySource(value = "classpath:person.properties")
public class Person {
    private String mail;
    
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
```

测试`@PropertySource`注解不支持注入`yml`文件。

扩展： `@ImportResource`：该注解导入`Spring`的`xml`配置文件，让配置文件里面的内容生效。

例如： `@ImportResource(locations = {"classpath:beans.xml"})`

通过上面的介绍，可以发现不管是使用 `YAML` 还是 `Properties` 都可以进行配置文件的编写，但是还不知道具体的使用方式，通过下面的几个注解，可以让我们了解到这些配置的具体使用方式。

#### `ConfigurationProperties`
`@ConfigurationProperties` 注解是 `Spring Boot` 提供的一种使用属性的注入方法。不仅可以方便的把配置文件中的属性值与所注解类绑定，还支持松散绑定，JSR-303 数据校验等功能。以上面演示的 `Properties` 的配置为例演示 `@ConfigurationProperties` 注解的使用。

```
@Data
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
    private String lastName;
    private Integer age;
    private Date birth;
    private Map<String, String> maps;
    private List<String> lists;
    private Dog dog;
    /**
     * 支持数据校验
     */
    
    private String email;

}
```

* `@Data` 是 `Lombok` 的注解，会为这个类所有属性添加 `getting` 和 `setting` 方法，此外还提供了`equals`、`canEqual`、`hashCode`、`toString` 方法。
* `@Component` 自动添加 `bean` 到 `spring` 容器中。
* `@ConfigurationProperties` 告诉这个类的属性都是配置文件里的属性，`prefix` 指定读取配置文件的前缀。


#### `Value`
`@Value` 支持直接从配置文件中读取值，同时支持 `SpEL` 表达式，但是不支持复杂数据类型和数据验证，下面是具体的使用。

```
public class PersonValue {
    /**
     * 直接从配置文件读取一个值
     */
    ("${person.last-name}")
    private String lastName;
    
    /**
     * 支持SpEL表达式
     */
    ("#{11*4/2}")
    private Integer age;

    ("${person.birth}")
    private Date birth;

    /**
     * 不支持复杂类型
     */
    private Map<String, String> maps;
    private List<String> lists;
    private Dog dog;

    /**
     * 不支持数据校验
     */
    
    ("xxx@@@@")
    private String email;
}
```

#### `@ConfigurationProperties` 和 `@Value`的区别。

|        特征        | @ConfigurationProperties |  @Value  |
| ----------------- | ------------------------ |----------|
| 功能               |  批量注入配置文件属性        |一个一个注入|
| 松散绑定（松散的语法）|  支持                       | 不支持   |
| SpEL              |  不支持                     |支持       |
| JSR-303 数据校验   |  支持                    |不支持      |
| 复杂类型           |  支持                     |不支持     |


`@ConfigurationProperties` 和 `@Value`的使用场景。

如果说，只是在某个业务逻辑中获取配置文件的某个值，使用 `@Value`.

如果说，专门编写有一个 `Java Bean` 来和配置文件映射，使用 `@ConfigurationProperties`.


#### `PropertySource`
随着业务复杂性的增加，配置文件也越来越多，我们会觉得所有的配置都写在一个 `properties` 文件会使配置显得繁杂不利于管理，因此希望可以把映射属性类的配置单独的抽取出来。由于 `Spring Boot` 默认读取`application.properties`，因此在抽取之后之前单独的`@ConfigurationProperties(prefix = "person")`已经无法读取到信息。这是可以使用 `@PropertySource `注解来指定要读取的配置文件。

需要注意的是，使用 `@PropertySource` 加载自定义的配置文件，，由于 `@PropertySource` 指定的文件会优先加载，所以如果在 `applocation.properties` 中存在相同的属性配置，会覆盖前者中对于的值。

如果抽取 `person` 配置为单独文件`domain-person.properties`。

### 多环境配置
在开发`Spring Boot`应用时，通常同一套程序会被应用和安装到几个不同的环境，比如：开发、测试、生产等。其中每个环境的数据库地址、服务器端口等等配置都会不同，如果在为不同环境打包时都要频繁修改配置文件的话，那必将是个非常繁琐且容易发生错误的事。

对于多环境的配置，各种项目构建工具或是框架的基本思路是一致的，通过配置多份不同环境的配置文件，再通过打包命令指定需要打包的内容之后进行区分打包，Spring Boot也不例外，或者说更加简单。

在S`pring Boot`中多环境配置文件名需要满足`application-{profile}.properties`的格式，其中`{profile}`对应你的环境标识，比如：

* application-dev.properties：开发环境
* application-test.properties：测试环境
* application-prod.properties：生产环境

至于哪个具体的配置文件会被加载，需要在`application.properties`文件中通过`spring.profiles.active`属性来设置，其值对应`{profile}`值。

如：`spring.profiles.active=test`就会加载`application-test.properties`配置文件内容

#### `properties` 多环境
那么如何在配置文件中激活其他的配置文件呢？只需要在 application.properties 启用其他文件。

```
# 激活 application-prod.properties文件
spring.profiles.active=prod
```

#### `YAML` 多环境
如果是使用 `YAML` 配置文件，我们可以使用文件块的形式，在一个 `YAML` 文件就可以达到多文件配置的效果，下面是 `Spring Boot` 使用 `YAML` 文件进行多环境配置的方式。

```
server:
  port: 8083
  profiles:
    active: dev # 指定环境为dev
# 使用三个---进行文档块区分

server:
  port: 8084
spring:
  profiles: dev

server:
  port: 8085
spring:
  profiles: prod
```

#### 多环境激活方式

除了以上的两种配置文件激活方式之外，还有另外两种种激活方式。

命令行 ，运行时添加 `--spring.profiles.active=prod`
`Jvm` 参数 ，运行时添加 `-Dspring.profiles.active=prod`
如果需要激活其他的配置文件，可以使用 `spring.config.location=G:/application.properties` 进行配置。

下面，以不同环境配置不同的服务端口为例，进行样例实验。

针对各环境新建不同的配置文件`application-dev.properties`、`application-test.properties`、`application-prod.properties`

在这三个文件均都设置不同的`server.port`属性，如：`dev`环境设置为`1111`，`test`环境设置为`2222`，`prod`环境设置为`3333`

`application.properties`中设置`spring.profiles.active=dev`，就是说默认以`dev`环境设置

测试不同配置的加载

执行`java -jar xxx.jar`，可以观察到服务端口被设置为`1111`，也就是默认的开发环境（dev）
执行`java -jar xxx.jar --spring.profiles.active=test`，可以观察到服务端口被设置为`2222`，也就是测试环境的配置`（test）`
执行`java -jar xxx.jar --spring.profiles.active=prod`，可以观察到服务端口被设置为3333，也就是生产环境的配置`（prod）`
按照上面的实验，可以如下总结多环境的配置思路：

`application.properties`中配置通用内容，并设置`spring.profiles.active=dev`，以开发环境为默认配置
`application-{profile}.properties`中配置各个环境不同的内容
通过命令行方式去激活不同环境的配置

### 总结
一、`Spring Boot` 支持两种格式的配置文件，其中`YAML`的数据结构比`properties`更清晰。

二、`YAML` 是专门用来写配置文件的语言，非常简洁和强大。

三、`YAML` 对空格的要求很严格，且不能用`Tab`键代替。

四、`YAML` 通过空格缩进的程度确定层级，冒号后面有空格，短横线后面有空格。

五、`ConfigurationProperties`注解适合批量注入配置文件中的属性，`Value`注解适合获取配置文件中的某一项。

六、`ConfigurationProperties`注解支持数据校验和获取复杂的数据，`Value`注解支持`SpEL`表达式。

### 附录
* [Spring Boot 配置文件](https://www.codingme.net/2019/01/springboot/springboot02-config/)
* [最全面的SpringBoot配置文件详解](https://zhuanlan.zhihu.com/p/57693064)
* [Properties and Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-properties-and-configuration.html)
* [Attack-spring-boot-2：spring-boot 配置解析](https://www.zybuluo.com/zhuanxu/note/1055545)
* [Spring Boot 加载配置多种方式](http://www.jerome.xin/articles/spring-boot-load-properties-method)
* [Spring Boot 2.0 配置图文教程](https://www.bysocket.com/technique/2135.html)
* [Spring Boot 之配置文件详解](http://www.bysocket.com/?p=1786)
* [Spring Boot配置文件详解](https://www.cnblogs.com/itdragon/p/8686554.html)
* [Springboot 系列（二）Spring Boot 配置文件](https://www.codingme.net/2019/01/springboot/springboot02-config/)
* [Spring Boot 配置文件详解：自定义属性、随机数、多环境配置等](http://www.spring4all.com/article/248)
* [SpringBoot 配置文件详解](https://www.jianshu.com/p/60b34464ca58)
* [史上最全的Spring Boot配置文件详解](https://www.cnblogs.com/yueshutong/p/10025820.html)