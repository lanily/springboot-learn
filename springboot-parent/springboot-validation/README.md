# [如何在项目中优雅的校验参数](https://segmentfault.co![img](https://avatar-static.segmentfault.com/112/666/1126665822-5d26b32ea4770_huge128)


## 前言

验证数据是贯穿所有应用程序层(从表示层到持久层)的常见任务。通常在每一层实现相同的验证逻辑，这既费时又容易出错。为了避免重复这些验证，开发人员经常将验证逻辑直接捆绑到域模型中，将域类与验证代码混在一起，这些验证代码实际上是关于类本身的元数据，与业务逻辑不相关。
![application-layers](https://segmentfault.com/img/bVbx309)

JSR 380——Bean Validation2.0——定义了用于实体和方法验证的元数据模型和API，将数据校验逻辑通过注解的形式封装在实体对象中。
![application-layers2](https://segmentfault.com/img/bVbx31a)

## 1.关于JSR

JSR是Java Specification Requests的缩写，意思是Java 规范提案。是指向[JCP](https://link.segmentfault.com/?url=https%3A%2F%2Flink.jianshu.com%3Ft%3Dhttp%3A%2F%2Fbaike.baidu.com%2Fview%2F148425.htm)(Java Community Process)提出新增一个标准化技术规范的正式请求。任何人都可以提交JSR，以向Java平台增添新的API和服务。JSR已成为Java界的一个重要标准。

JSR-303 是JAVA EE 6 中的一项子规范，后来的版本是Bean Validation 1.1（JSR-349），目前最新版本是Bean Validation 2.0（JSR-380），**Hibernate Validator** 是 Bean Validation 的参考实现 ，除了Jakarta Bean验证API定义的约束之外，Hibernate Validator还有一些附加的 constraint；**并且spring-boot-starter-web默认集成了Hibernate Validator**。（**springboot2.3版本已经移除hibernate-validator的依赖，需要手动引入**）
![image-20201203102541552](https://segmentfault.com/img/bVcLh31)

## 2.为什么使用Hibernate Validator

- 提高代码整洁度;
- 验证逻辑与业务逻辑之间进行了分离，降低了程序耦合度；
- 统一且规范的验证方式，无需你再次编写重复的验证代码；
- 你将更专注于你的业务，将这些繁琐的事情统统丢在一边。

## 3.注解介绍

### JSR 380内置常用注解

| **注解**                      | **详细信息**                                                 |
| ----------------------------- | ------------------------------------------------------------ |
| `@Null`                       | 被注释的元素必须为 `null`                                    |
| `@NotNull`                    | 被注释的元素必须不为 `null`                                  |
| `@AssertTrue`                 | 被注释的元素必须为 `true`                                    |
| `@AssertFalse`                | 被注释的元素必须为 `false`                                   |
| `@Min(value)`                 | 被注释的元素可以是字符串、数值类型，如果元素是字符串类型，将值转为BigDecimal类型，并与value属性进行比对，值必须大于等于指定的value值 |
| `@Max(value)`                 | 被注释的元素可以是字符串、数值类型，如果元素是字符串类型，将值转为BigDecimal类型，并与value属性进行比对，值必须小于等于指定的value值 |
| `@DecimalMin(value)`          | 被注释的元素可以是字符串、数值（可以带小数点），将注解内value的值转为BigDecimal类型，必须大于等于指定的最小值（可以配置是否等于value，默认是包含的） |
| `@DecimalMax(value)`          | 被注释的元素可以是字符串、数值（可以带小数点），将注解内value的值转为BigDecimal类型，其值必须小于等于指定的最大值（可以配置是否等于value，默认是包含的） |
| `@Size(max, min)`             | 被注释的元素的大小必须在指定的范围内，可用于字符串、Collection、Map、数组等类型 |
| `@Digits (integer, fraction)` | 被注释的元素必须是一个数字，其值必须在可接受的范围内         |
| `@Past`                       | 被注释的元素必须是一个过去的日期                             |
| `@Future`                     | 被注释的元素必须是一个将来的日期                             |
| `@Pattern(value)`             | 被注释的元素必须符合指定的正则表达式                         |
| `@Email`                      | 被注释的元素必须是电子邮箱地址                               |
| `@NotBlank`                   | 验证字符串非null，且trim后长度必须大于0                      |
| `@NotEmpty`                   | 适用于String、Collection、Map或者数组不能为Null且长度或元素个数必须大于0 |
| @Valid                        | 具体作用下面会列举                                           |

### Hibernate Validator 附加的 constraint

| **注解**          | **详细信息**                                                 |
| ----------------- | ------------------------------------------------------------ |
| `@Length`         | 被注释的字符串的大小必须在指定的范围内                       |
| `@URL`            | 根据RFC2396标准校验注释的字符串必须是一个的有效的url         |
| `@Range`          | 被注释的元素必须在合适的范围内，应用于数值或字符串           |
| `@UniqueElements` | 检查带注释的集合是否只包含唯一的元素。相等性是使用equals()方法确定的。 |
| `@SafeHtml`       | 检查带注释的值是否包含潜在的恶意片段，如`<script/>`。如用这个注解需要引入jsoup的依赖，用来解析html代码 |

### 注意

- @NotNull ：适用于任何类型被注解的元素，必须不能为NULL
- @NotEmpty ：适用于String、Collection、Map或者数组，不能为Null且长度或元素个数必须大于0
- @NotBlank：验证字符串非null，且trim后长度必须大于0

**@Validated与@Valid的区别：**

- @Validated注解是spring提供的，提供了一个分组功能，可以在入参验证时，根据不同的分组采用不同的验证机制。没有添加分组属性时，默认验证没有分组的验证属性（**Default分组**）；
- @Validated：可以用在类型、方法和方法参数上，**但是不能用在成员属性（字段）上**；
- @Validated： 用在方法入参上无法单独提供嵌套验证功能，也无法提示框架进行嵌套验证。能配合嵌套验证注解@Valid进行嵌套验证。
- @Valid：作为标准JSR-303规范，还没有吸收分组的功能；
- @Valid：可以用在方法、方法参数、构造函数、**方法参数和成员属性（字段）上**；
- @Valid加在方法参数时并不能够自动进行嵌套验证，而是用在需要嵌套验证类的相应字段上，来配合方法参数上@Validated或@Valid来进行嵌套验证。

## 4.使用

由于spring-boot-starter-web（**springboot 2.3以下版本**）依赖默认集成了Hibernate Validator，所以无需添加任何依赖和相关配置，只需要在项目中引入spring-boot-starter-web依赖即可（演示springboot版本为2.1.2.RELEASE），由于要用到@SafeHtml注解，这里需要加上jsoup的依赖。

```java
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
 <!-- 解析html片段-->
<dependency>
  <groupId>org.jsoup</groupId>
  <artifactId>jsoup</artifactId>
  <version>1.8.3</version>
</dependency>
```

Hibernate Validator有两种校验模式：

- 普通模式(会校验完所有的属性，然后返回所有的验证失败信息，默认是这个模式)

- 快速失败返回模式(**只要有一个字段验证失败，就返回结果**)

  在@Configuration Class中配置以下代码，将Validator设置为快速失败返回模式

  ```java
  @Bean
      public Validator validator(){
          ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class)
                  .configure()
                  .addProperty( "hibernate.validator.fail_fast", "true" )
                  .buildValidatorFactory();
          Validator validator = validatorFactory.getValidator();
          return validator;
      }
  ```

### a.对象校验

1.在对象中添加注解

```java
@Data
public class User {
    //注解对静态变量不生效
    @NotBlank(message = "性别不能为空")
    private static String sex;
    
    @NotBlank(message = "姓名不能为空")
    @Size(min = 2,max = 5,message = "姓名长度不规范")
    private String name;
    
    @NotNull(message = "年龄不能为空")
    @Max(value = 30,message = "年龄超过最大值30")
    @Range(min=30,max=60)
    private Integer age;
    
    @DecimalMax(value = "108.88",message = "超过最大108.88",inclusive = false)
    private Double price;
    
    @Past(message = "生日不能大于当前日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birthday;
    
    @Email(message = "电子邮箱格式不正确")
    private String email;
    
    @SafeHtml(message = "非法请求参数")
    private String content;
}
```

2.进入Controller对应方法，在需要校验的对象前添加@Valid注解即可（**校验对静态变量不生效**），在使用 `@Valid` 注解的参数后可以紧跟着一个 `BindingResult` 类型的参数，用于获取校验结果（将校验结果封装在BingdingResult对象中，不会抛出异常）

注意：**@Valid 和 BindingResult 是一一对应的，如果有多个@Valid，那么每个@Valid后面跟着的BindingResult就是这个@Valid的验证结果，顺序不能乱**

```java
    //单个对象校验
    @PostMapping("user")
    //校验参数后边跟BindingResult,spring不会抛出异常,将校验结果封装在这个对象中
    public String person(@Valid User user,BindingResult bindingResult){
        System.out.println(user);
        StringBuilder sb = new StringBuilder();
        if(bindingResult.hasErrors()){
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for(ObjectError error:allErrors){
                sb.append(error.getDefaultMessage()+",");
            }
        }
        return sb.toString();
    }
```

3.如果此时去掉实体对象后面的BindingResult，如校验未通过会抛出**BindException**异常，需要在全局异常处理器中捕获并统一处理

4.全局异常处理器配置

```java
@RestControllerAdvice
@Slfj
@AutoConfigurationPackage
public class GlobalExceptionHandler {
    //spring-context包里面的异常
    //实体对象前不加@RequestBody注解,单个对象内属性校验未通过抛出的异常类型
    @ExceptionHandler(BindingException.class)
    public ResponseEntity<ExceptionResponseVO> methodArguments(BindingException e){
        log.warn("throw BindingException,{}",e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponseVO.error(NEError.INVALID_PARAMETER, e.getBindingResult().getFieldError().getDefaultMessage()));
    }
    
       //实体对象前不加@RequestBody注解,校验方法参数或方法返回值时,未校验通过时抛出的异常
    //Validation-api包里面的异常
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionResponseVO> methodArguments(ValidationException e){
        log.warn("throw ValidationException,{}",e);
       return ResponseEntity.status(HttpStatus.BAD_REQUEST)            .body(ExceptionResponseVO.error(NEError.INVALID_PARAMETER,e.getCause().getMessage()));
    }
    
    //spring-context包里面的异常,实体对象前加@RequestBody注解,抛出的异常为该类异常
    //方法参数如果带有@RequestBody注解，那么spring mvc会使用RequestResponseBodyMethodProcessor      //对参数进行序列化,并对参数做校验
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseVO> methodArguments(MethodArgumentNotValidException e){
        log.warn("throw MethodArgumentNotValidException,{}",e);
       return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponseVO.error(NEError.INVALID_PARAMETER,                e.getBindingResult().getFieldError().getDefaultMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity methodArguments(Exception e){
        log.warn("throw exception,{}",e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
```

### b.级联校验

如果一个对象内部包含另一个对象作为属性，属性上加 @Valid，可以验证作为属性的对象内部的验证

```java
@Data
public class User2 {
    @NotBlank(message = "姓名不能为空")
    private String name;
    @Max(value = 50,message = "年龄不能为空")
    private Integer age;
    @Valid
    @NotNull(message = "商品不能为空")
    private Goods goods;
}
@Data
public class Goods{
    @NotBlank(message = "商品名称不能为空")
    private String goodsName;
    @NotNull(message = "商品价格不能为空")
    private Double goodsPrice;
}
```

如果级联校验内元素的属性校验未通过，抛出**MethodArgumentNotValidException**异常，注意在全局异常处理器捕获该异常并处理

```java
//级联校验
 @PostMapping("cascade")
public String cascade(@Valid @RequestBody User2 user2){
     return "OK";
 }
```

### c.容器元素校验

用来校验实体对象内集合中的元素，在容器泛型前加注解，可实现对容器单个元素的校验；如下：

```java
@Data
public class User3 {
    @NotBlank(message = "姓名不能为空")
    private String name;
    @Max(value = 50,message = "年龄不能为空")
    private Integer age;
    
    @Valid
    @NotEmpty(message = "商品列表不能为空")
    private List<@NotNull(message = "商品不能为空") Goods> goodsList;
}
```

如果容器元素校验未通过，抛出异常**MethodArgumentNotValidException**（与级联校验抛出的一样）

```java
//容器元素校验
@PostMapping("container")
public String container(@Valid @RequestBody User3 user3){
    return "OK";
}
```

### d.方法的校验

JSR 303标准定义接口ExecutableValidator，用来校验方法参数，Hibernate Validator实现了该接口（ValidatorImpl.class），不仅对Object的属性进行校验，还可以对方法参数、返回值、构造函数参数等进行校验；Spring 在此基础上进行了扩展，添加了MethodValidationPostProcessor拦截器，通过AOP实现对方法的校验；此时抛出的异常是**javax.validation.ConstraintViolationException**

注意 ：**必须在Controller上面加上注解@Validated，否则校验规则无效**

```java
@RestController
@RequestMapping("validator")
@Validated
public class ValidatorController {
    @GetMapping("demo1")
    public String test1(@Range(min = 1,max = 999,message = "起始笔数超过区间范围")@RequestParam int pageIndex, @Range(min = 1,max = 999,message = "查询笔数超过区间范围"）@RequestParam int pageSize){
        return "ok";
    }
}
```

除了校验Controller方法外，也可校验Service（**必须是单例的bean，否则不生效，因为方法参数校验逻辑底层用AOP来实现**）等方法，用法如下：

```java
@Service
@Validated
public class UserService {
    //校验方法参数
    public String queryUserName(@NotNull(message = "用户参数不能为空") User user){
        return user.getName();
    }
    
    //校验方法返回值
    @NotNull(message = "用户信息不存在")
    public User queryUser(User user){
        return null;
    }
}
```

### e.分组校验的实现

#### 分组

同一个校验规则，不可能适用于所有的业务场景，对每一个业务场景去编写一个校验规则，又显得特别冗余。实际上我们可以用到Hibernate-Validator的分组功能，达到对不同场景做出不同的校验逻辑，减少DTO对象的创建。

比如一个User对象，新增的时候不需要检验id（系统生成），修改的时候需要检验id属性，要想复用Class，就可以使用Hibernate Validator的分组。

实例代码：

```java
@Data
public class UserGroup {
    @NotNull(message = "id不能为空",groups = UpdateUser.class)
    private Integer id;
    @NotBlank(message = "姓名不能为空",groups = AddUser.class)
    private String name;
    @NotNull(message = "年龄不能为空",groups = AddUser.class)
    private Integer age;

    public interface AddUser{}
    public interface UpdateUser{}
}
```

添加用户：在需要校验的对象前面加@Validated注解（**不能使用@Valid注解**），并配置分组class，此时AddUser的分组校验规则生效。

```java
    //分组校验:添加用户
    @PostMapping("addUser")
    public String addUser(@Validated(UserGroup.AddUser.class) UserGroup userGroup){
        return "OK";
    }
```

修改用户：配置UpdateUser分组

```java
    //分组校验:修改用户
    @PostMapping("updateUser")
    public String updateUser(@Validated(UserGroup.UpdateUser.class) UserGroup userGroup){
        return "OK";
    }
```

使用分组能极大的复用需要验证的Class信息，而不是按业务重复编写冗余的类。

**注意**：如果指定了校验组，则该属性将不再属于默认的校验组Default.class，则在省略校验组参数的情况下，将不会校验自定义校验组的属性。

#### 组序列

除了按组指定是否验证之外，还可以指定组的验证顺序，前面组验证不通过的，后面组不进行验证；其中@GroupSequence提供组序列的形式进行顺序式校验，即先校验@Save分组的，如果校验不通过就不进行后续的校验分组了。我认为顺序化的校验，场景更多的是在业务处理类，例如联动的属性验证，值的有效性很大程度上不能从代码的枚举或常量类中来校验。

实例代码：

```java
@Data
public class UserDTO {
    @NotNull(message = "id不能为空",groups = {UpdateUser.class})
    private Integer id;
    @NotBlank(message = "姓名不能为空",groups = {AddUser.class})
    private String name;
    @NotNull(message = "年龄不能为空",groups = {AddUser.class})
    private Integer age;
    @NotNull(message = "版本不能为空")//不配置goups，默认就是Default分组
    private Integer version;

    @GroupSequence({AddUser.class, UpdateUser.class, Default.class})
    public interface AddUpdateGroup{}
    public interface AddUser{}
    public interface UpdateUser{}
}
```

首先校验AddUser分组的注解，如果AddUser校验不通过，就不会去校验UpdateUser和Default的分组

```java
@PostMapping("user")
public String saveUser(@Validated(UserDTO.AddUpdateGroup.class) UserDTO userDTO){
    userMapper.addUser(userDTO);
    return "ok";
}
```

## 5.自定义constraint

一般情况，自定义验证可以解决很多问题；某些业务场景下又需要做一些特别的参数校验，此时，我们可以实现validator的接口，自定义验证器。

创建自定义注解@Sex，该注解是放在字段上的，也可以根据业务场景放在方法或者Class上面）用于判断性别是否符合约束

```java
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SexConstraintValidator.class)
public @interface Sex {

    String message() default "性别参数有误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };
}
```

创建自定义验证器

```java
public class SexConstraintValidator
        implements ConstraintValidator<Sex,String> {
    /**
     * 性别约束逻辑
     * @param value
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        //如果value为null,那么该校验规则不生效;可搭配@NotNull注解使用,更加灵活
        if(value == null){
            return true;
        }
        return "男".equals(value) || "女".equals(value);
    }
}
```

要验证的DTO对象

```java
@Data
public class UserDTO {
    @NotNull(message = "id不能为空")
    private Integer id;
    @NotBlank(message = "姓名不能为空")
    private String name;
    @NotNull(message = "年龄不能为空")
    private Integer age;
    @NotNull(message = "版本不能为空")
    private Integer version;
    @Sex
    private String sex;
}
```

在UserDTO对象前加@Valid注解，可实现对性别字段的合法性校验，sex只能传入“男“或“女”。

这只是一个小例子，大家可以根据业务场景自定义参数校验器，例如敏感词校验、预防sql注入、js脚本攻击等等，都可以用自定义校验器来完成。

赞1收藏

[分享](https://segmentfault.com/a/1190000038401180###)