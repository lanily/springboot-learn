## 一、Mock的使用背景

单元测试的思路就是我们想在不涉及依赖关系的情况下测试代码。

在单元测试中，我们往往想去独立地去测一个类中的某个方法，但是这个类可不是独立的，它会去调用一些其它类的方法和service，这也就导致了以下两个问题：

- 外部服务可能无法在单元测试的环境中正常工作，因为它们可能需要访问数据库或者调用其它Http服务。
- 我们的测试关注点在于这个类的实现上，外部类的一些行为可能会影响到我们对本类的测试，那也就失去了我们进行单测的意义。

先来看看下面这个示例：我们要对A进行测试，那么就要先把整个依赖树构建出来，也就是BCDE的实例。

![img](https://pdai.tech/_images/develop/ut/ut-dev-mock-4.png)

一种替代方案就是将依赖mock掉, 把测试的重心完全放在目标类A上

![img](https://pdai.tech/_images/develop/ut/ut-dev-mock-5.png)

Mock测试就是在测试过程中，对那些不容易构建的对象，用一个虚拟对象来代替测试的情形。

**说白了：**就是解耦(虚拟化)要测试的目标方法中调用的其它方法，例如：Service的方法调用Mapper类的方法，这时候就要把Mapper类Mock掉（产生一个虚拟对象），

这样我们可以自由的控制这个Mapper类中的方法，让它们返回想要的结果、抛出指定异常、验证方法的调用次数等等。

## 二、常用Mock框架

**Mockito 和 PowerMock**

**Mockito**是一个优秀的、最常用的单元测试mock框架，它能满足大部分时间的测试要求（public方法）；

**PowerMock**可以去解决一些更难的问题（比如静态方法、私有方法、Final方法等）。

PowerMock 是在 EasyMock 以及 Mockito 基础上的扩展，通过提供定制的类加载器以及一些字节码篡改，实现更强大的测试功能。

## 三、Mockito的使用实例

***1、测试类初始化***

测试目标类：

***![img](https://blog.csdn.net/download/attachments/89722123/image2018-7-9+20%3A9%3A19.png)***

**初始化方式有两种：**

**（1）注解**

***![img](https://blog.csdn.net/download/attachments/89722123/image2018-7-9+20%3A10%3A31.png)***

```
@RunWith(MockitoJUnitRunner.class) 换成@RunWith(PowerMockRunner.class)也可以支持这些注解。
```

@Mock相当于：NeedMockClass mockInstatnce = Mockito.mock(NeedMockClass.class);
还有一种@Spy后面提到，等价于NeedMockClass spyInstatnce = Mockito.spy(new NeedMockClass());

被测试类上标记@InjectMocks，Mockito就会实例化该类，并将标记@Mock、@Spy注解的属性值注入到被测试类中。

注意**@InjectMocks的注入顺序：**

- 如果这里的TargetClass中没有显示定义构造方法，Mockito会调用默认构造函数实例化对象，然后依次寻找setter 方法 或 属性（按Mock对象的类型或名称匹配）注入@Mock对象；

- 如果TargetClass中显式定义了有参数的构造函数，那么 就不再寻找setter 方法和 属性注入， Mockito会选择参数个数最多的构造函数实例化并注入@Mock对象(
  这样可以尽可能注入多的属性)；

但是有多个最大构造函数时，Mockito 究竟选择哪一个就混乱了，测试时应该避免这种情况的发生，很容易发生空指针。

![img](https://blog.csdn.net/download/attachments/89722123/image2018-7-13+14%3A51%3A24.png)

如上图：此时invokeService 和 invokeMapper肯定有一个是null

**（2）类反射**

***![img](https://blog.csdn.net/download/attachments/89722123/image2018-7-9+20%3A25%3A25.png)***

***2、Mockito实例***

**public方法测试实例**

```java
//测试目标类：
@Service
public class MsgLogService implements IMsgLogService {

    @Resource
    private MsgLogMapper msgLogMapper;
    @Resource
    private IZhwxApiService zhwxApiService;

    @Override
    public void handle() {
        List<MsgLog> list = msgLogMapper.selectUnHandledMsg();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        for (MsgLog log : list) {
            if (StringUtils.isEmpty(log.getMsg())) {
                continue;
            }

            boolean res = zhwxApiService.putProfileToHBase(log.getMsg());
            if (res) {
                msgLogMapper.updateHandleStatus(log);
            }
        }
    }
}

// 测试类：
@RunWith(MockitoJUnitRunner.class)
public class MsgLogServiceTest {

    @InjectMocks
    private MsgLogService msgLogService;
    @Mock
    private MsgLogMapper msgLogMapper;

    @Mock
    private IZhwxApiService zhwxApiService;


    @Test
    public void handle_request_times_0_01() {
        Mockito.when(msgLogMapper.selectUnHandledMsg()).thenReturn(null);
        msgLogService.handle();
        Mockito.verify(zhwxApiService, Mockito.times(0)).putProfileToHBase(Mockito.anyString());
        Mockito.verify(msgLogMapper, Mockito.times(0)).updateHandleStatus(Mockito.any());
    }

}

// 注：when()、any()、verify()等都是Mockito类中的静态方法，推荐将这些静态方法导入，就可以在测试类中直接引用了

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;
```

***3、Mock的发生时机: Stubbing***

*当以下语法出现时，Mock就发生了，此时称作设置测试桩（Stubbing）*

```java
when(mockMapper.insert(any())).thenReturn(888);
        when(mockMapper.insert(any())).thenThrow(new RuntimeException("db操作异常"));
        when(mockService.methodReturnId(any(OrderInfo.class))).thenAnswer(demoAnswer);
        doReturn(888).when(mockMapper).insert(any());
```

上面mock的方法都是有返回值的，为void函数设置桩用以下语法，因为编译器不喜欢void函数在括号内

```java
doNothing().when(mockService).voidMethod(any(String.class),any(Integer.class));
        doThrow(new RuntimeException("")).when(mockService).voidMethod(eq("ex"),eq(10001));
        doAnswer(demoAnswer).when(mockService).methodVoid(any(OrderInfo.class));
```

**Stubbing连缀调用：**

```java
// 第一次调用返回1，第二次调用返回2，以下三种写法等价的:
when(mockService.addStr(anyString())).thenReturn("1").thenReturn("2");
        when(mockService.addStr(anyString())).thenReturn("1","2");
        doReturn("1").doReturn("2").when(mockService).addStr(anyString());
        String relt1=mockService.addStr("x");
        String relt2=mockService.addStr("x");
        String relt3=mockService.addStr("x");
        Assert.assertEquals("1",relt1);
        Assert.assertEquals("2",relt2);
        Assert.assertEquals("2",relt3);//后续调用一直返回2
// 第一次调用什么也不做，第二次调用抛出异常:
        doNothing().doThrow(new RuntimeException("调用两次了")).when(mockService).methodVoid(any());
        mockService.methodVoid(any());
        try{
        mockService.methodVoid(any());
        }catch(Exception e){
        Assert.assertEquals("调用两次了",e.getMessage());

        }
// 下面写法结果就变了，第二次stubbing覆盖第一次的:
        when(mockService.addStr(anyString())).thenReturn("1");
        when(mockService.addStr(anyString())).thenReturn("2");
        String relt1=mockService.addStr("x");
        String relt2=mockService.addStr("x");
        Assert.assertEquals("2",relt1);
        Assert.assertEquals("2",relt2);
```

***4、Mock方法的默认值***

*Mock对象的方法未\*设置测试桩时，\*Mockito会返回方法返回类型的默认值，不会报错。mock 实例默认的会给所有的方法添加基本实现：返回 null 或空集合，或者 0
等基本类型的值。这取决于方法返回类型*

```java
List mockList=mock(List.class);
        when(mockList.get(5)).thenReturn("hello");//打桩
        Assert.assertEquals("hello",mockList.get(5));//打桩的情景返回设定值
        Assert.assertEquals(null,mockList.get(10));//未打桩的情景不会报错，返回默认值
```

***5、参数匹配器（matchers）***

是一些静态方法，说白了就是mock方法成立的一些条件

列举几个典型的匹配器：

`any()`: 任何参数
`any(OrderInfo.class)`: 任何OrderInfo（开发中自定义的类）
`anyString()` :任何字符串，等同于any(String.class)
`eq(1): 具体值1`

```java
//InvokeService类中，要被mock的方法
public Integer targetMethod01(String param01,Integer param02){
        return 1;
        }
//测试类TargetClassTest中：
@Mock
private InvokeService mockService;
@Test
public void dmeoTest(){
        when(mockService.targetMethod01(any(),any())).thenReturn(666);
        when(mockService.targetMethod01(any(String.class),anyInt())).thenReturn(666);
        when(mockService.targetMethod01("demo",1)).thenReturn(666);
        when(mockService.targetMethod01(eq("demo"),eq(1))).thenReturn(666);
        //上面都是正确的，下面的写法，单测执行时会报错
        when(mockService.targetMethod01(eq("demo"),1)).thenReturn(666);
        }
//一旦使用了参数匹配器来验证，那么所有参数都应该使用参数匹配
```

***6、行为测试***

前面提到的 when(……).thenReturn(……) 等属于**状态测试**，某些时候，测试不关心返回结果，而是侧重方法有否被正确的参数调用过。从概念上讲，就是和状态测试所不同的“行为测试”了。

一旦使用 mock() 或@Mock生成模拟对象，意味着 Mockito 会记录着这个模拟对象调用了什么方法，还有调用了多少次、调用的顺序等。最后由用户决定是否需要进行验证。

***6-1、验证Mock方法的调用次数***

*几个常用的方法：*

`times(x)`：ck方法被调用x次

`never()`：从未被调用过，等价于times(0)

`atLeast(x)`：至少调用过x次

`atLeastOnce()`：至少调用过1次，等价于atLeast(1)

`atMost(x)`：最多调用过x次

*verify 内部跟踪了所有的方法调用和参数的调用情况，然后会返回一个结果，说明是否通过。*

*verify 也可以像 when 那样使用参数匹配器。*

```Java
//目标类TargetClass中：
@Autowired
private InvokeService invokeService;
public void invokeTimes(){
        invokeService.addStr("1");
        invokeService.addStr("2");
        invokeService.addStr("2");
        }
//测试类TargetClassTest中：
@InjectMocks
TargetClass targetClass;
@Mock
private InvokeService mockService;
@Test
public void testTimes(){
        targetClass.invokeTimes();
        verify(mockService).addStr(eq("1"));//times()省略的话，默认验证调用一次
        verify(mockService,times(1)).addStr(eq("1"));
        verify(mockService,times(1)).addStr("1");
        verify(mockService,times(2)).addStr("2");
        verify(mockService,atLeastOnce()).addStr("1");
        verify(mockService,atMost(2)).addStr("2");
        verify(mockService,never()).addStr("0");
        }
```

***6-2、验证Mock方法的调用顺序***

```java
//目标类TargetClass中：
@Autowired
private InvokeService invokeService;
public void addStr(){
        invokeService.add("str01");
        invokeService.add("str02");
        }

//测试类TargetClassTest中：
@InjectMocks
TargetClass targetClass;
@Mock
private InvokeService mockService;
@Test
public void testInOrder_verify(){
        InOrder inOrder=inOrder(mockService);
        targetClass.addStr();
        //验证调用顺序，若是调换两句，将会出错
        inOrder.verify(mockService).add("str01");
        inOrder.verify(mockService).add("str02");
        }
```

***7、Answer***

主要用来截获传递给mock方法的参数

典例：保存一个订单，调用mapper的insert( )方法，保存到数据库的OrderInfo是测试目标方法saveOrder( )中的一个局部变量，

怎样获取到这个OrderInfo，从而验证插入数据库的字段赋值是否正确？

**Answer获取方法参数实例**

```java
//目标类TargetClass中：
@Autowired
private InvokeService invokeService;
@Autowired
private InvokeMapper invokeMapper;
public int saveOrder(){
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setUserName("sxd");
        orderInfo.setDealerId(62669);
        orderInfo.setOrderType(3);
        int result=invokeMapper.insert(orderInfo);
        return result;
        }

//InvokeMapper中：
        int insert(OrderInfo orderInfo);
//测试类TargetClassTest中：
@InjectMocks
TargetClass targetClass;
@Mock
private InvokeService mockService;
@Mock
private InvokeMapper mockMapper;

//定义一个Answer类，用于截获mock方法入参
class DemoAnswer implements Answer<Object> {

    Object[] params;

    @Override
    public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
        params = invocationOnMock.getArguments();//方法入参
        return 666;//方法返回值
    }

    public Object[] getParams() {
        return params;
    }
}

    @Test
    public void test_insertOrder_param_by_Answer() throws Exception {
        DemoAnswer demoAnswer = new DemoAnswer();
        doAnswer(demoAnswer).when(mockMapper).insert(any(OrderInfo.class));
        int result = targetClass.saveOrder();
        Assert.assertEquals(666, result);
        OrderInfo saveOrder = (OrderInfo) demoAnswer.getParams()[0];
        Assert.assertEquals(3, saveOrder.getOrderType().intValue());
    }
```

**有返回值的方法demo**

```java
//InvokeService类中，需要mock的方法：
public Integer methodReturnId(OrderInfo orderInfo)throws Exception{
        return orderInfo.getId();
        }
//测试类TargetClassTest中：
@Mock
private InvokeService mockService;
@Test
public void testAnswer_when_method_unvoid()throws Exception{
        Answer<Integer> demoAnswer=new Answer<Integer>(){
@Override
public Integer answer(InvocationOnMock invocationOnMock)throws Throwable{
        OrderInfo orderInfo=(OrderInfo)invocationOnMock.getArguments()[0];//methodReturnId方法的入参
        //orderInfo.setId(2);//截获并自定义入参
        if(orderInfo.getId()>3){
        throw new RuntimeException("大于3了");
        }
        return 666;//自定义methodReturnId方法的返回值
        }
        };
//mock的方法是非void时，以下三种写法都可以
//doAnswer(answer).when(mockService).methodReturnId(any(OrderInfo.class));//doAnswer：执行demoAnswer的answer方法
//when(mockService.methodReturnId(any(OrderInfo.class))).then(demoAnswer);//then(answer): 执行demoAnswer的answer方法 when(mockService.methodReturnId(any(OrderInfo.class))).thenAnswer(demoAnswer);//thenAnswer(answer): 执行demoAnswer的answer方法
        OrderInfo paramOrder=new OrderInfo(){{
        setId(3);
        }};
        Integer result=mockService.methodReturnId(paramOrder);
        Assert.assertEquals(666,result.intValue());
        OrderInfo paramOrder02=new OrderInfo(){{
        setId(4);
        }};
        try{
        mockService.methodReturnId(paramOrder02);
        }catch(Exception e){
        Assert.assertEquals("大于3了",e.getMessage());
        }
        }
```

**void方法demo**

```java
//InvokeService中，需要mock的方法：
public void methodVoid(OrderInfo orderInfo){
        }
//测试类中：
@Mock
private InvokeService mockService;
@Test
public void testAnswer_when_method_void()throws Exception{
        Answer<Integer> demoAnswer=new Answer<Integer>(){
@Override
public Integer answer(InvocationOnMock invocationOnMock)throws Throwable{
        OrderInfo orderInfo=(OrderInfo)invocationOnMock.getArguments()[0];//methodReturnId方法的入参
        //orderInfo.setId(2);
        if(orderInfo.getId()>3){
        throw new RuntimeException("大于3了");
        }
        return null;
        }
        };
        //void方法时，只能这样写
        doAnswer(demoAnswer).when(mockService).methodVoid(any(OrderInfo.class));//doAnswer：执行demoAnswer的answer方法
        mockService.methodVoid(new OrderInfo(){{
        setId(3);
        }});
        }
```

***8、void方法的mock***

对void方法进行方法预期设定

```java
doNothing().when(mockService).voidMethod(anyString(),any(Integer.class));//什么也不做
        doThrow(new RuntimeException("")).when(mockService).voidMethod(eq("ex"),eq(10001));//抛出指定异常
        doNothing().doThrow(new RuntimeException("")).when(mockService).voidMethod(anyString(),any(Integer.class));//第一次调用什么也不做，第二次调用抛异常
```

## 四、PowerMock的使用实例

***1、PowerMock简单实现原理***

当某个类被注解**@PrepareForTest**
标注以后，在运行测试用例时，会创建一个新的org.powermock.core.classloader.MockClassLoader实例，然后加载该测试用例使用到的类（系统类除外）。

PowerMock会根据你的mock要求，去修改写在注解@PrepareForTest里的class文件（当前测试类会自动加入注解中），以满足特殊的mock需求。**
例如：去除final方法的final标识，在静态方法的最前面加入自己的虚拟实现等。**

如果需要mock的是系统类的final方法和静态方法，PowerMock不会直接修改系统类的class文件，而是修改调用系统类的class文件，以满足mock需求。

PowerMock支持Mockito PowerMock有两个重要的注解：

@RunWith(PowerMockRunner.class)

@PrepareForTest( { YourClassWithEgStaticMethod.class })

如果你的测试用例里没有使用注解@PrepareForTest，那么可以不用加注解@RunWith(PowerMockRunner.class)
，反之亦然。当你需要使用PowerMock强大功能（Mock静态、final、私有方法等）的时候，就需要加注解@PrepareForTest。

***2、基本用法***

只是需要调用mock对象的public方法，其实此时使用Mockito就可以了

```java
//目标类：
public class TargetClass {

    @Autowired
    private InvokeService invokeService;
    @Autowired
    private InvokeMapper invokeMapper;

    public TargetClass() {
    }

    public TargetClass(InvokeMapper invokeMapper) {
        this.invokeMapper = invokeMapper;
    }

    public int saveToDb() {
        return invokeMapper.insert(new OrderInfo());
    }
}

//测试类：
//@RunWith(PowerMockRunner.class) //未mock静态、私有、final等方法时可以不需要Runner
public class TargetClassPowerMockTest {

    @Test
    public void test_common_method() {
        //PowerMock中生成mock对象
        InvokeMapper mockMapper = PowerMockito.mock(InvokeMapper.class);
        PowerMockito.when(mockMapper.insert(Mockito.any())).thenReturn(666);//支持Mockito中的参数匹配器
        TargetClass targetClass = new TargetClass(mockMapper);
        int result = targetClass.saveToDb();
        Assert.assertEquals(666, result);
    }
}

//也可以结合使用注解：
@RunWith(PowerMockRunner.class)
public class TargetClassPowerMockTest {

    @InjectMocks
    TargetClass targetClass;
    @Mock
    private InvokeService mockService;
    @Mock
    private InvokeMapper mockMapper;

    @Test
    public void test_common_method_use_annotation() {
        PowerMockito.when(mockMapper.insert(Mockito.any())).thenReturn(666);//支持Mockito中的参数匹配器
        int result = targetClass.saveToDb();
        Assert.assertEquals(666, result);
    }
}
```

***3、Mock静态方法***

测试类必须启用 @PrepareForTest 和 @RunWith注解，否则运行报错

```java
//目标类：
public class TargetClass {

    public String getImgUrl() {
        return InvokeClass.getUrl();//调用静态方法
    }
}

//静态方法所在的类：
public class InvokeClass {

    public static String getUrl() {
        return "";
    }
}

//测试类：
@RunWith(PowerMockRunner.class)
@PrepareForTest({InvokeClass.class})//注解里InvokeClass是静态方法所在的类
public class TargetClassPowerMockTest {

    /**
     * mock静态方法
     */
    @Test
    public void test_static_method() throws Exception {
        PowerMockito.mockStatic(InvokeClass.class);//mock静态方法所在的类
        PowerMockito.when(InvokeClass.getUrl()).thenReturn("www.abc.com");//打桩静态方法返回值
        TargetClass targetClass = new TargetClass();
        String url = targetClass.getImgUrl();
        /*验证静态方法调用次数，两行代码要成对出现*/
        PowerMockito.verifyStatic();//验证静态方法被调用一次
        InvokeClass.getUrl();//上面验证次数的静态方法
        PowerMockito.verifyStatic(Mockito.times(1));//验证静态方法被调用一次
        InvokeClass.getUrl();//上面验证次数的静态方法
        Assert.assertEquals("www.abc.com", url);
    }
}
```

***4、私有方法***

***4-1 Mock私有方法***

测试类必须启用 @PrepareForTest 和 @RunWith注解，否则运行报错

```java
//目标类TargetClass中：
public Integer callPrivateMethod(Integer param){
        return isAPrivateMethod(new OrderInfo());//调用私有方法
        }
private Integer isAPrivateMethod(OrderInfo order){
        if(order==null){
        return 111;
        }
        int result=invokeMapper.insert(order);
        if(result==1){
        return 666;
        }else{
        return 222;
        }
        }

//测试类：
@RunWith(PowerMockRunner.class)
@PrepareForTest({TargetClass.class})//注解里TargetClass是私有方法所在的类
public class TargetClassPowerMockTest {

    /**
     * mock私有方法
     * 全部mock
     */
    @Test
    public void test_private_method_use_mock() throws Exception {
        TargetClass mockTargetClass = PowerMockito
                .mock(TargetClass.class);//mock掉了TargetClass的所有方法，而我们希望的只是mock被调用的私有方法

        PowerMockito.when(mockTargetClass.callPrivateMethod(Mockito.anyInt()))
                .thenCallRealMethod();//设置callPrivateMethod不被mock，走真实的方法体；否则返回的是对应类型的默认值
        PowerMockito.when(mockTargetClass, "isAPrivateMethod", Mockito.any())
                .thenReturn(888);//打桩私有方法isAPrivateMethod
        Assert.assertEquals(888, mockTargetClass.callPrivateMethod(1).intValue());
        PowerMockito.verifyPrivate(mockTargetClass, Mockito.times(1))
                .invoke("isAPrivateMethod", Mockito.any());//验证调用了1次该私有方法
    }

    /**
     * mock私有方法
     * 部分mock
     */

    @Test
    public void test_private_method_use_spy() throws Exception {
        //为TargetClass创建一个监控(spy)对象，该对象中未设置测试桩的方法，仍然走真实的方法体
        TargetClass spyTargetClass = PowerMockito.spy(new TargetClass());
        //当给spy的类方法设桩时，最好使用doReturn等，使用thenReturn等可能会产生一些错误
        PowerMockito.doReturn(888).when(spyTargetClass, "isAPrivateMethod", Mockito.any());//打桩私有方法
        Assert.assertEquals(888, spyTargetClass.callPrivateMethod(1).intValue());
        PowerMockito.verifyPrivate(spyTargetClass, Mockito.times(1))
                .invoke("isAPrivateMethod", Mockito.any());//验证调用了1次该私有方法
    }
}
```

**mock：** 全部mock, 所有没有调用when设置过的方法，在测试时调用，返回的都是对应返回类型的默认值。

**spy：**部分mock, 意思是你可以修改某个真实对象的某些方法的行为特征，而不改变他的基本行为特征, 只是设桩（when设置）的方法被mock，未设桩的方法走真实逻辑。

***4-2 同时mock静态和私有方法***

测试类必须启用 @PrepareForTest 和 @RunWith注解，否则运行报错

```java
//目标类TargetClass中：
public Integer callStaticAndPrivateMethod(){
        String url=InvokeClass.getUrl();//调用静态方法
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setIp(url);
        Integer result=isAPrivateMethod(orderInfo);//调用私有方法
        result=isAPrivateMethod(orderInfo);//调用私有方法
        return result;
        }

private Integer isAPrivateMethod(OrderInfo order){
        if(order==null){
        return 111;
        }
        int result=invokeMapper.insert(order);
        if(result==1){
        return 666;
        }else{
        return 222;
        }
        }
//静态方法所在的类InvokeClass中：
public static String getUrl(){
        return"";
        }

//测试类：
@RunWith(PowerMockRunner.class)
@PrepareForTest({InvokeClass.class, TargetClass.class})
//注解里InvokeClass是静态方法所在的类，TargetClass是私有方法所在的类
public class TargetClassPowerMockTest {

    /**
     * 同时mock静态、私有方法
     */
    @Test
    public void test_staticAndPrivate_method() throws Exception {
        PowerMockito.mockStatic(
                InvokeClass.class);//mock静态方法所在的类 PowerMockito.when(InvokeClass.getUrl()).thenReturn("www.autohome.com");//打桩静态方法返回值
        TargetClass spyTargetClass = PowerMockito.spy(new TargetClass());
        PowerMockito.doReturn(888).when(spyTargetClass, "isAPrivateMethod", Mockito.any());//打桩私有方法
        DemoAnswer demoAnswer = new DemoAnswer();
        PowerMockito.doAnswer(demoAnswer)
                .when(spyTargetClass, "isAPrivateMethod", Mockito.any());//截获私有方法参数
        Integer result = spyTargetClass.callStaticAndPrivateMethod();//调用目标方法
        Assert.assertEquals(666, result.intValue());//验证方法返回值
        PowerMockito.verifyPrivate(spyTargetClass, Mockito.times(2))
                .invoke("isAPrivateMethod", Mockito.any());//验证调用了2次该私有方法
        OrderInfo saveOrder = (OrderInfo) demoAnswer.getParams()[0];//截获参数
        Assert.assertEquals("www.autohome.com", saveOrder.getIp());
    }

    class DemoAnswer implements Answer<Object> {

        Object[] params;

        @Override
        public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
            params = invocationOnMock.getArguments();//方法入参
            return 666;//方法返回值
        }

        public Object[] getParams() {
            return params;
        }
    }
}
```

***4-3 测试私有方法逻辑***

使用类反射原理，Mockito就可以实现，无需PowerMock

```java
//目标类TargetClass中：
public Integer callPrivateMethod(Integer param){
        return isAPrivateMethod(new OrderInfo());//调用私有方法
        }

private Integer isAPrivateMethod(OrderInfo order){
        if(order==null){
        return 111;
        }
        int result=invokeMapper.insert(order);
        if(result==1){
        return 666;
        }else{
        return 222;
        }
        }
//测试类中：

/**
 * 测试私有方法逻辑
 */
@Test
public void test_private_method_Logic()throws Exception{
        InvokeMapper mockMapper=Mockito.mock(InvokeMapper.class);
        TargetClass targetClass=new TargetClass(mockMapper);
        Method privateMethod=targetClass.getClass().getDeclaredMethod("isAPrivateMethod",OrderInfo.class);
        privateMethod.setAccessible(true);
        OrderInfo orderInfo=null;
        Integer result01=(Integer)privateMethod.invoke(targetClass,orderInfo);
        Assert.assertEquals(111,result01.intValue());
        orderInfo=new OrderInfo();
        Mockito.when(mockMapper.insert(Mockito.any())).thenReturn(1);
        Integer result02=(Integer)privateMethod.invoke(targetClass,orderInfo);
        Assert.assertEquals(666,result02.intValue());
        }
```

***5、Mock final方法***

文档中说测试类必须启用 @PrepareForTest 和 @RunWith注解，但在实际操作中，不加这两个注解仍然可以mock成功，而且使用Mockito也可以mock出final方法

```java
//目标类：
public class TargetClass {

    public String getAppId(InvokeClass invokeClass) {
        return invokeClass.getAppId();//调用final方法
    }
}

//final方法所在的类：
public class InvokeClass {

    public final String getAppId() {
        return "dealer";
    }
}

//PowerMock测试类：
@RunWith(PowerMockRunner.class)
@PrepareForTest({InvokeClass.class})//注解里InvokeClass是final方法所在的类, 但此处两个注解可不加
public class TargetClassPowerMockTest {

    /**
     * final方法
     */

    @Test
    public void test_final_method() throws Exception {
        InvokeClass mockInvokeClass = PowerMockito.mock(InvokeClass.class);//mock final方法所在的类
        PowerMockito.when(mockInvokeClass.getAppId()).thenReturn("dealercloud");//打桩,改写final方法的返回值
        TargetClass targetClass = new TargetClass();
        String appId = targetClass.getAppId(mockInvokeClass);
        Assert.assertEquals("dealercloud", appId);
    }
}

//Mokito测试类：
@RunWith(MockitoJUnitRunner.class)
public class TargetClassTest {

    @InjectMocks
    TargetClass targetClass;

    @Test
    public void test_final_method() {
        InvokeClass mockInvokeClass = Mockito.mock(InvokeClass.class);//mock final方法所在的类
        Mockito.when(mockInvokeClass.getAppId()).thenReturn("dc");//打桩,改写final方法的返回值
        String appId = targetClass.getAppId(mockInvokeClass);
        Assert.assertEquals("dc", appId);
    }
}
```

***6、Mock系统类的静态方法***

测试类必须启用 @PrepareForTest 和 @RunWith注解，否则运行报错。

和Mock普通类的静态方法、final方法一样，只不过注解**@PrepareForTest**里写的类不一样 ，是需要调用系统方法所在的类。

```java
//目标类TargetClass中：
public String callSysStaticMethod(String param){
        return System.getProperty(param);//调用系统类的静态方法
        }

//测试类：
@RunWith(PowerMockRunner.class)
@PrepareForTest({TargetClass.class})//注解里TargetClass是调用系统类静态方法的类
public class TargetClassPowerMockTest {

    /**
     * mock系统类的静态方法
     */
    @Test
    public void test_sysStatic_method() {
        PowerMockito.mockStatic(
                System.class);//mock系统类 PowerMockito.when(System.getProperty("hello")).thenReturn("autohome");//打桩系统类的静态方法
        TargetClass targetClass = new TargetClass();
        String result = targetClass.callSysStaticMethod("hello");
        Assert.assertEquals("autohome", result);
    }
}
```

***7、Mock方法内局部对象（构造方法）***
测试类必须启用 @PrepareForTest 和 @RunWith注解，否则PowerMockito.whenNew( )。。。不生效

```java
//目标类：
public class TargetClass {

    public boolean chekFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }
}

//测试类：
@RunWith(PowerMockRunner.class)
@PrepareForTest({TargetClass.class})//注解里TargetClass是mock的new对象代码所在的类
public class TargetClassPowerMockTest {

    /**
     * mock局部对象
     */
    @Test
    public void test_when_new_inMethod() throws Exception {
        File mockFile = PowerMockito.mock(File.class);//mock出File对象
        PowerMockito.when(mockFile.exists()).thenReturn(true);
        PowerMockito.whenNew(File.class).withArguments("mypath")
                .thenReturn(mockFile);//方法内部new出的对象指向mockFile
        TargetClass targetClass = new TargetClass();
        boolean result = targetClass.chekFileExist("mypath");
        Assert.assertTrue(result);
    }
}
```

## 五、总结

Mockito和PowerMock使用语法有很大类似, 以上实例基本能覆盖大部分测试要求，具体根据测试需求，优先使用Mockito框架，

并且实际测试中，测试方法命名要规范，单纯的从方法名就能看出这个测试的目的（入参、结果），不要担心因此造成的命名过长。

由于mock测试主要侧重于代码逻辑的测试，对于DAO层覆盖不到，所以方法中用到的sql语句要自己手动执行一遍，起码能保证没有语法错误。

**最后附上其它同事的类似总结wiki：**

[我是这样写Java单元测试的](https://blog.csdn.net/pages/viewpage.action?pageId=83660608)

[JAVA单元测试](https://blog.csdn.net/pages/viewpage.action?pageId=82532638)