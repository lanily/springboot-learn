## 什么是 Mock 测试

> Mock通常是指，在测试一个对象A时，我们构造一些假的对象来模拟与A之间的交互，而这些Mock对象的行为是我们事先设定且符合预期。通过这些Mock对象来测试A在正常逻辑，异常逻辑或压力情况下工作是否正常。@pdai

Mock 测试就是在测试过程中，对于某些不容易构造（如 HttpServletRequest 必须在Servlet 容器中才能构造出来）或者不容易获取比较复杂的对象（如 JDBC 中的ResultSet 对象），用一个虚拟的对象（Mock 对象）来创建以便测试的测试方法。Mock 最大的功能是帮你把单元测试的耦合分解开，如果你的代码对另一个类或者接口有依赖，它能够帮你模拟这些依赖，并帮你验证所调用的依赖的行为。

先来看看下面这个示例：

![img](https://pdai.tech/_images/develop/ut/ut-dev-mock-4.png)

从上图可以看出如果我们要对A进行测试，那么就要先把整个依赖树构建出来，也就是BCDE的实例。

一种替代方案就是使用mocks

![img](https://pdai.tech/_images/develop/ut/ut-dev-mock-5.png)

从图中可以清晰的看出, mock对象就是在调试期间用来作为真实对象的替代品。

mock测试就是在测试过程中，对那些不容易构建的对象用一个虚拟对象来代替测试的方法就叫mock测试。

## Mock 适用在什么场景

> 在使用Mock的过程中，发现Mock是有一些通用性的，对于一些应用场景，是非常适合使用Mock的：

- 真实对象具有不可确定的行为(产生不可预测的结果，如股票的行情)
- 真实对象很难被创建(比如具体的web容器)
- 真实对象的某些行为很难触发(比如网络错误)
- 真实情况令程序的运行速度很慢
- 真实对象有用户界面
- 测试需要询问真实对象它是如何被调用的(比如测试可能需要验证某个回调函数是否被调用了)
- 真实对象实际上并不存在(当需要和其他开发小组，或者新的硬件系统打交道的时候，这是一个普遍的问题)

当然，也有一些不得不Mock的场景：

- 一些比较难构造的Object：这类Object通常有很多依赖，在单元测试中构造出这样类通常花费的成本太大。
- 执行操作的时间较长Object：有一些Object的操作费时，而被测对象依赖于这一个操作的执行结果，例如大文件写操作，数据的更新等等，出于测试的需求，通常将这类操作进行Mock。
- 异常逻辑：一些异常的逻辑往往在正常测试中是很难触发的，通过Mock可以人为的控制触发异常逻辑。

在一些压力测试的场景下，也不得不使用Mock，例如在分布式系统测试中，通常需要测试一些单点（如namenode，jobtracker）在压力场景下的工作是否正常。而通常测试集群在正常逻辑下无法提供足够的压力（主要原因是受限于机器数量），这时候就需要应用Mock去满足。


##  测试类的分类

- **dummy object** 做为参数传递给方法但是绝对不会被使用。譬如说，这种测试类内部的方法不会被调用，或者是用来填充某个方法的参数。
- **Fake** 是真正接口或抽象类的实现体，但给对象内部实现很简单。譬如说，它存在内存中而不是真正的数据库中。（译者注：**Fake** 实现了真正的逻辑，但它的存在只是为了测试，而不适合于用在产品中。）
- **stub** 类是依赖类的部分方法实现，而这些方法在你测试类和接口的时候会被用到，也就是说 **stub** 类在测试中会被实例化。**stub** 类会回应任何外部测试的调用。**stub** 类有时候还会记录调用的一些信息。
- **mock object** 是指类或者接口的模拟实现，你可以自定义这个对象中某个方法的输出结果。

测试替代技术能够在测试中模拟测试类以外对象。因此你可以验证测试类是否响应正常。譬如说，你可以验证在 Mock 对象的某一个方法是否被调用。这可以确保隔离了外部依赖的干扰只测试测试类。

我们选择 Mock 对象的原因是因为 Mock 对象只需要少量代码的配置。


## 1 Mockito 介绍 [3]



### 1.1 Mockito是什么？

Mockito是mocking框架，它让你用简洁的API做测试。而且Mockito简单易学，它可读性强和验证语法简洁。

> Mockito是最流行的Java mock框架之一.

除了Mockito以外，还有一些类似的框架，比如：

- EasyMock：早期比较流行的MocK测试框架。它提供对接口的模拟，能够通过录制、回放、检查三步来完成大体的测试过程，可以验证方法的调用种类、次数、顺序，可以令 Mock 对象返回指定的值或抛出指定异常
- PowerMock：这个工具是在EasyMock和Mockito上扩展出来的，目的是为了解决EasyMock和Mockito不能解决的问题，比如对static, final, private方法均不能mock。其实测试架构设计良好的代码，一般并不需要这些功能，但如果是在已有项目上增加单元测试，老代码有问题且不能改时，就不得不使用这些功能了
- JMockit：JMockit 是一个轻量级的mock框架是用以帮助开发人员编写测试程序的一组工具和API，该项目完全基于 Java 5 SE 的 java.lang.instrument 包开发，内部使用 ASM 库来修改Java的Bytecode

Mockito已经被广泛应用，所以这里重点介绍Mockito。

Mockito 是一个用于 Java 单测的 Mock 框架，除了 JUnit 之外，它还可以用于其他单测框架（例如：TestNG）。`Mockito` 可以改变一个类或者对象的行为，能够让我们更加专注地去测试代码逻辑，省去了构造数据所花费的努力。



### 1.2 为什么需要Mock

测试驱动的开发( TDD)要求我们先写单元测试，再写实现代码。在写单元测试的过程中，我们往往会遇到要测试的类有很多依赖，这些依赖的类/对象/资源又有别的依赖，从而形成一个大的依赖树，要在单元测试的环境中完整地构建这样的依赖，是一件很困难的事情。如下图所示：

![img](https://images2015.cnblogs.com/blog/484791/201701/484791-20170120134044703-96948251.png)

为了测试类A，我们需要Mock B类和C类（用虚拟对象来代替）如下图所示：

![img](https://images2015.cnblogs.com/blog/484791/201701/484791-20170120134311703-125872357.png)

### 1.3 Stub和Mock异同 [1]

- 相同点：Stub和Mock对象都是用来模拟外部依赖，使我们能控制。

- 不同点：而stub完全是模拟一个外部依赖，用来提供测试时所需要的测试数据。而mock对象用来判断测试是否能通过，也就是用来验证测试中依赖对象间的交互能否达到预期。在mocking框架中mock对象可以同时作为stub和mock对象使用，两者并没有严格区别。 更多信息：http://martinfowler.com/articles/mocksArentStubs.html



### 1.4 Mockito资源

- 官网： [http://mockito.org](http://mockito.org/)

- API文档：http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html

- 项目源码：https://github.com/mockito/mockito

- Mockito 官方网站: https://site.mockito.org/

- PowerMockito Github: https://github.com/powermock/powermock/



### 1.5 使用场景

- 提前创建测试; TDD（测试驱动开发）
- 团队可以并行工作
- 你可以创建一个验证或者演示程序
- 为无法访问的资源编写测试
- Mock 可以交给用户
- 隔离系统





### 1.6 基本概念

Mock 可分为两种类型，一种是 **Class Mock**，另一种是 **Partial Mock**（Mockito 叫 **spy**）。
改变 mock 对象方法（method）的行为叫 **Stub**（插桩）。
一次 Mock 过程称为 **Mock Session**，它会记录所有的 **Stubbing**，基本包含如下三个步骤：

```
+----------+      +------+      +--------+
| Mock/Spy | ===> | Stub | ===> | Verify |
+----------+      +------+      +--------+
```



名词解释：

- stub：存根。即配置mock对象的某个方法被调用时该返回什么样的结果的过程。
- 交互：发生了某个方法的调用。
- spy：监视，间谍。
- **存根**(或者说是**打桩**），指的是对某个方法指定返回策略的操作（具体表现为两种：1指定返回值，2使用doCallRealMethod()或者thenCallRealMethod()指定当方法被调用时执行实际代码逻辑），功能就是当测试执行到此方法时直接返回我们指定的返回值（此时不会执行此方法的实际代码逻辑）或者执行此方法的实际代码逻辑并返回

测试中我们创建的对象一般可以分为三种：**被测对象**、**mock对象**和**spy对象**。

首先我们明确一下这三种对象的概念：

1. 被测对象：即我们想要测试的对象，比如xxService、xxUtils等。
2. mock对象：一般为我们被测对象的依赖对象。典型如被测对象的成员变量。主要是一些测试中我们不关注的对象。我们只想要得到这些对象的方法的返回值。而不关注这些方法的具体执行逻辑。此时我们可以将这些对象创建为mock对象。
3. spy对象：在Mockito中它是基于**部分mock**概念提出的。spy对象也可由mock对象使用特定参数下创建。也就是说：**spy对象其实是一种特殊的mock对象。**和mock对象一样，它可以作为被测对象的依赖对象。此时它和mock对象的最大的区别是mock对象的方法如果没有被存根，调用时会返回相应对象的空值（下文有详细介绍）；而spy对象的方法被调用时则会调用真实的代码逻辑。

#### Class Mock

Class Mock 改变了 class 的行为，所以 mock 出来的对象就完全失去了原来的行为。
如果没有对 method 进行插桩，那么 method 会返回默认值（`null`、`false`、`0`等）。

最基本的用法如下：

```
import static org.mockito.Mockito.*;

// 利用 List.class 创建一个 mock 对象 --- mockedList
List mockedList = mock(List.class);

// 操作 mockedList
mockedList.add("one");
mockedList.clear();

// 验证
verify(mockedList).add("one");
verify(mockedList).clear();
```



#### Partial Mock（spy）

如果只是想改变一个实例（instance）的行为，我们需要使用 `spy`：

```
List list = new LinkedList();
List spy = spy(list);

// optionally, you can stub out some methods:
when(spy.size()).thenReturn(100);

// using the spy calls *real* methods
spy.add("one");
spy.add("two");

// prints "one" - the first element of a list
System.out.println(spy.get(0));

// size() method was stubbed - 100 is printed
System.out.println(spy.size());

// optionally, you can verify
verify(spy).add("one");
verify(spy).add("two");
```

如果方法没有被插桩，那么它的行为就不会被改变。所以有些情况下，我们不能使用 `when(Object)` 进行插桩，只能使用 do 系列方法（`doReturn|Answer|Throw()`）：

```
List list = new LinkedList();
List spy = spy(list);

// Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException 
// (the list is yet empty)
when(spy.get(0)).thenReturn("foo");

// You have to use doReturn() for stubbing
doReturn("foo").when(spy).get(0);
```

------

**Mockito 的静态方法 spy 和 mock 的区别**

`spy` 是 partial mock，所以其本质上也是 `mock`，通过源码可以得知：

```
public static <T> T spy(T object) {
  return MOCKITO_CORE.mock((Class<T>) object.getClass(), withSettings()
    .spiedInstance(object)
    .defaultAnswer(CALLS_REAL_METHODS));
}
```

> `MOCKITO_CORE` 是 `Mockito` 的核心实现类，`spy` 和 `mock` 方法一样，都是调用了 `MOCKITO_CORE.mock`。

```
public static <T> T mock(Class<T> classToMock) {
  return mock(classToMock, withSettings());
}

public static <T> T mock(Class<T> classToMock, MockSettings mockSettings) {
  return MOCKITO_CORE.mock(classToMock, mockSettings);
}

public static MockSettings withSettings() {
  return new MockSettingsImpl().defaultAnswer(RETURNS_DEFAULTS);
}
```

通过代码可以看出，`spy` 和 `mock` 最大的区别在于使用了不同的 `MockSettings`，`spy` 的 `MockSettings` 需要传入一个 `spiedInstance`。

`spy` 的默认 Answer 是 `CALLS_REAL_METHODS`，也就是说，如果一个方法没有被 stub，那么会执行它真实的行为。
`mock` 的默认 Answer 是 `RETURNS_DEFAULTS`，没有被 stub 的方法会返回一个默认值。



#### Stub（插桩）

仅仅 Mock 出来一个对象显然是不够的，虽然可以通过验证**方法的执行情况**来测试代码的逻辑，但是多数情况下我们还需要改变方法的返回值，这时就需要用到“插桩”。

```
LinkedList mockedList = mock(LinkedList.class);

// stubbing
when(mockedList.get(0)).thenReturn("first");
when(mockedList.get(1)).thenThrow(new RuntimeException());

// 打印出 "first"
System.out.println(mockedList.get(0));
// 抛出异常
System.out.println(mockedList.get(1));
// 返回 null，因为 get(999) 没有被 stub
System.out.println(mockedList.get(999));
```

##### Stubbing

Stubbing 表示一次插桩，它的形式是 `when(x).then(y)`，用于指定 mock 的行为。

*Stubbing 的示例代码*

```
when(mock.foo()).thenReturn(true);
```



可以通过如下代码获取 mock 对象的所有 stubbing：

```
Mockito.mockingDetails(mock).getStubbings();
```

Stubbing 的接口规范如下：

```
public interface Stubbing {
    Invocation getInvocation();
    boolean wasUsed();
}
```

- `getInvocation()` 返回被插桩的方法调用，例如，`mock.foo()` 就是一个 `Invocation`。
- `wasUsed()` 用于表示 stubbing 是否被使用，如果 `mock.foo()` 没有被调用，那么 `wasUsed()` 返回 false，说明存在未被使用的 stubbing，为了保持 `clarity of tests`，最好删除未被使用的 stubbing 代码。

**Stubbing关系图**

```
   +-----------------+
   | OngoingStubbing |
   +-----------------+
           /|\
            |
        implements
            |
            |
   +-----------------+
   |   BaseStubbing  |<-----------------+
   +-----------------+                  | 
           /|\                          |
            |                           |
         extends                     extends 
            |                           |
            |                           |
+----------------------+    +---------------------+
| ConsecutiveStubbing  |    | OngoingStubbingImpl |
+----------------------+    +---------------------+
```

#### Stubber

Stubber 是一个插装器。

当我们用 `doThrow()|doAnswer()|doNothing()|doReturn()` 的形式进行插桩时，可以通过 Stubber 来选择 mock 对象的方法：

*示例一*

```
doThrow(new RuntimeException()).when(mockedList).clear();
```



*示例二*

```
doThrow(new RuntimeException("one"))
    .doThrow(new RuntimeException("two"))
        .when(mock).someVoidMethod();
```



#### Answer

不管是 `Stubber` 还是 `OngoingStubbing`，除了标准返回之外，都提供了自定义返回值的方法：

- `Stubber doAnswer(Answer answer);`
- `OngoingStubbing<T> then(Answer<?> answer);`

Answer 的接口定义如下：

```
public interface Answer<T> {
    T answer(InvocationOnMock invocation) throws Throwable;
}
```

如下代码利用 `Answer` 改变了 mock 方法的行为：

```
when(mock.someMethod(anyString())).thenAnswer(
    new Answer() {
        public Object answer(InvocationOnMock invocation) {
            Object[] args = invocation.getArguments();
            Object mock = invocation.getMock();
            return "called with arguments: " + Arrays.toString(args);
    }
});
```

那么 `mock.someMethod("foo")` 的返回值就变成了 `called with arguments: [foo]`。

`Answer` 不接受参数，只有返回值，Mockito 还提供了其他 5 个 Answer，分别接受不同个数的参数，然后返回一个值。

- Answer1
- Answer2
- Answer3
- Answer4
- Answer5

只接受参数，没有返回值的 Answer 包括：

- VoidAnswer1
- VoidAnswer2
- VoidAnswer3
- VoidAnswer4
- VoidAnswer5



## 2 使用Mockito [2][4]

添加maven依赖

```xml
      <dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-core</artifactId>
  <version>3.7.7</version>
  <scope>test</scope>
</dependency>
```

添加junit依赖

```xml
      <dependency>
  <groupId>junit</groupId>
  <artifactId>junit</artifactId>
  <version>4.12</version>
  <scope>test</scope>
</dependency>
```

添加引用

```
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
```

### 2.1 验证行为



```
    @Test
    public void verify_behaviour(){
        //模拟创建一个List对象
        List mock = mock(List.class);
        //使用mock的对象
        mock.add(1);
        mock.clear();
        //验证add(1)和clear()行为是否发生
        verify(mock).add(1);
        verify(mock).clear();
    }
```



### 2.2 模拟我们所期望的结果



```
    @Test
    public void when_thenReturn(){
        //mock一个Iterator类
        Iterator iterator = mock(Iterator.class);
        //预设当iterator调用next()时第一次返回hello，第n次都返回world
        when(iterator.next()).thenReturn("hello").thenReturn("world");
        //使用mock的对象
        String result = iterator.next() + " " + iterator.next() + " " + iterator.next();
        //验证结果
        assertEquals("hello world world",result);
    }
```





```
    @Test(expected = IOException.class)
    public void when_thenThrow() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        //预设当流关闭时抛出异常
        doThrow(new IOException()).when(outputStream).close();
        outputStream.close();
    }
```



### 2.3 RETURNS_SMART_NULLS和RETURNS_DEEP_STUBS

RETURNS_SMART_NULLS实现了Answer接口的对象，它是创建mock对象时的一个可选参数，mock(Class,Answer)。

在创建mock对象时，有的方法我们没有进行stubbing，所以调用时会放回Null这样在进行操作是很可能抛出NullPointerException。如果通过RETURNS_SMART_NULLS参数创建的mock对象在没有调用stubbed方法时会返回SmartNull。例如：返回类型是String，会返回"";是int，会返回0；是List，会返回空的List。另外，在控制台窗口中可以看到SmartNull的友好提示。



```
    @Test
    public void returnsSmartNullsTest() {
        List mock = mock(List.class, RETURNS_SMART_NULLS);
        System.out.println(mock.get(0));
        
        //使用RETURNS_SMART_NULLS参数创建的mock对象，不会抛出NullPointerException异常。另外控制台窗口会提示信息“SmartNull returned by unstubbed get() method on mock”
        System.out.println(mock.toArray().length);
    }
```



RETURNS_DEEP_STUBS也是创建mock对象时的备选参数

RETURNS_DEEP_STUBS参数程序会自动进行mock所需的对象，方法deepstubsTest和deepstubsTest2是等价的



```
    @Test
    public void deepstubsTest(){
        Account account=mock(Account.class,RETURNS_DEEP_STUBS);
        when(account.getRailwayTicket().getDestination()).thenReturn("Beijing");
        account.getRailwayTicket().getDestination();
        verify(account.getRailwayTicket()).getDestination();
        assertEquals("Beijing",account.getRailwayTicket().getDestination());
    }
    @Test
    public void deepstubsTest2(){
        Account account=mock(Account.class); 
        RailwayTicket railwayTicket=mock(RailwayTicket.class);        
        when(account.getRailwayTicket()).thenReturn(railwayTicket); 
        when(railwayTicket.getDestination()).thenReturn("Beijing");
        
        account.getRailwayTicket().getDestination();
        verify(account.getRailwayTicket()).getDestination();    
        assertEquals("Beijing",account.getRailwayTicket().getDestination());
    }    
    
    public class RailwayTicket{
        private String destination;

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }        
    }
    
    public class Account{
        private RailwayTicket railwayTicket;

        public RailwayTicket getRailwayTicket() {
            return railwayTicket;
        }

        public void setRailwayTicket(RailwayTicket railwayTicket) {
            this.railwayTicket = railwayTicket;
        }
    }
```



### 2.4 模拟方法体抛出异常

```
    @Test(expected = RuntimeException.class)
    public void doThrow_when(){
        List list = mock(List.class);
        doThrow(new RuntimeException()).when(list).add(1);
        list.add(1);
    }
```



### 2.5 使用注解来快速模拟

在上面的测试中我们在每个测试方法里都mock了一个List对象，为了避免重复的mock，是测试类更具有可读性，我们可以使用下面的注解方式来快速模拟对象：

```
    @Mock
    private List mockList;
```

OK，我们再用注解的mock对象试试

```
    @Test
    public void shorthand(){
        mockList.add(1);
        verify(mockList).add(1);
    }
```

运行这个测试类你会发现报错了，mock的对象为NULL，为此我们必须在基类中添加初始化mock的代码



```
public class MockitoExample2 {
    @Mock
    private List mockList;

    public MockitoExample2(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shorthand(){
        mockList.add(1);
        verify(mockList).add(1);
    }
}
```



或者使用built-in runner：MockitoJUnitRunner



```
@RunWith(MockitoJUnitRunner.class)
public class MockitoExample2 {
    @Mock
    private List mockList;

    @Test
    public void shorthand(){
        mockList.add(1);
        verify(mockList).add(1);
    }
}
```



### 2.6 参数匹配



```
    @Test
    public void with_arguments(){
        Comparable comparable = mock(Comparable.class);
        //预设根据不同的参数返回不同的结果
        when(comparable.compareTo("Test")).thenReturn(1);
        when(comparable.compareTo("Omg")).thenReturn(2);
        assertEquals(1, comparable.compareTo("Test"));
        assertEquals(2, comparable.compareTo("Omg"));
        //对于没有预设的情况会返回默认值
        assertEquals(0, comparable.compareTo("Not stub"));
    }
```



除了匹配制定参数外，还可以匹配自己想要的任意参数



```
    @Test
    public void with_unspecified_arguments(){
        List list = mock(List.class);
        //匹配任意参数
        when(list.get(anyInt())).thenReturn(1);
        when(list.contains(argThat(new IsValid()))).thenReturn(true);
        assertEquals(1, list.get(1));
        assertEquals(1, list.get(999));
        assertTrue(list.contains(1));
        assertTrue(!list.contains(3));
    }

    private class IsValid extends ArgumentMatcher<List>{
        @Override
        public boolean matches(Object o) {
            return o == 1 || o == 2;
        }
    }
```



注意：如果你使用了参数匹配，那么所有的参数都必须通过matchers来匹配，如下代码：



```
    @Test
    public void all_arguments_provided_by_matchers(){
        Comparator comparator = mock(Comparator.class);
        comparator.compare("nihao","hello");
        //如果你使用了参数匹配，那么所有的参数都必须通过matchers来匹配
        verify(comparator).compare(anyString(),eq("hello"));
        //下面的为无效的参数匹配使用
        //verify(comparator).compare(anyString(),"hello");
    }
```



### 2.7 自定义参数匹配



```
    @Test
    public void argumentMatchersTest(){
        //创建mock对象
        List<String> mock = mock(List.class);

        //argThat(Matches<T> matcher)方法用来应用自定义的规则，可以传入任何实现Matcher接口的实现类。
        when(mock.addAll(argThat(new IsListofTwoElements()))).thenReturn(true);

        mock.addAll(Arrays.asList("one","two","three"));
        //IsListofTwoElements用来匹配size为2的List，因为例子传入List为三个元素，所以此时将失败。
        verify(mock).addAll(argThat(new IsListofTwoElements()));
    }
    
    class IsListofTwoElements extends ArgumentMatcher<List>
    {
        public boolean matches(Object list)
        {
            return((List)list).size()==2;
        }
    }
```



### 2.8 捕获参数来进一步断言

较复杂的参数匹配器会降低代码的可读性，有些地方使用参数捕获器更加合适。



```
        @Test
    public void capturing_args(){
        PersonDao personDao = mock(PersonDao.class);
        PersonService personService = new PersonService(personDao);

        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        personService.update(1,"jack");
        verify(personDao).update(argument.capture());
        assertEquals(1,argument.getValue().getId());
        assertEquals("jack",argument.getValue().getName());
    }

     class Person{
        private int id;
        private String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    interface PersonDao{
        public void update(Person person);
    }

    class PersonService{
        private PersonDao personDao;

        PersonService(PersonDao personDao) {
            this.personDao = personDao;
        }

        public void update(int id,String name){
            personDao.update(new Person(id,name));
        }
    }
```



### 2.9 使用方法预期回调接口生成期望值（Answer结构）



```
@Test
    public void answerTest(){
        when(mockList.get(anyInt())).thenAnswer(new CustomAnswer());
        assertEquals("hello world:0",mockList.get(0));
        assertEquals("hello world:999",mockList.get(999));
    }

    private class CustomAnswer implements Answer<String>{
        @Override
        public String answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            return "hello world:"+args[0];
        }
    }
```



也可使用匿名内部类实现



```
    @Test
    public void answer_with_callback(){
        //使用Answer来生成我们我们期望的返回
        when(mockList.get(anyInt())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return "hello world:"+args[0];
            }
        });
        assertEquals("hello world:0",mockList.get(0));
        assertEquals("hello world:999",mockList.get(999));
    }
```



### 2.10 修改对未预设的调用返回默认期望



```
    @Test
    public void unstubbed_invocations(){
        //mock对象使用Answer来对未预设的调用返回默认期望值
        List mock = mock(List.class,new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return 999;
            }
        });
        //下面的get(1)没有预设，通常情况下会返回NULL，但是使用了Answer改变了默认期望值
        assertEquals(999, mock.get(1));
        //下面的size()没有预设，通常情况下会返回0，但是使用了Answer改变了默认期望值
        assertEquals(999,mock.size());
    }
```



### 2.11 用spy监控真实对象

- Mock不是真实的对象，它只是用类型的class创建了一个虚拟对象，并可以设置对象行为
- Spy是一个真实的对象，但它可以设置对象行为
- InjectMocks创建这个类的对象并自动将标记@Mock、@Spy等注解的属性值注入到这个中



```
    @Test(expected = IndexOutOfBoundsException.class)
    public void spy_on_real_objects(){
        List list = new LinkedList();
        List spy = spy(list);
        //下面预设的spy.get(0)会报错，因为会调用真实对象的get(0)，所以会抛出越界异常
        //when(spy.get(0)).thenReturn(3);

        //使用doReturn-when可以避免when-thenReturn调用真实对象api
        doReturn(999).when(spy).get(999);
        //预设size()期望值
        when(spy.size()).thenReturn(100);
        //调用真实对象的api
        spy.add(1);
        spy.add(2);
        assertEquals(100,spy.size());
        assertEquals(1,spy.get(0));
        assertEquals(2,spy.get(1));
        verify(spy).add(1);
        verify(spy).add(2);
        assertEquals(999,spy.get(999));
        spy.get(2);
    }
```



### 2.12 真实的部分mock

```
    @Test
    public void real_partial_mock(){
        //通过spy来调用真实的api
        List list = spy(new ArrayList());
        assertEquals(0,list.size());
        A a  = mock(A.class);
        //通过thenCallRealMethod来调用真实的api
        when(a.doSomething(anyInt())).thenCallRealMethod();
        assertEquals(999,a.doSomething(999));
    }


    class A{
        public int doSomething(int i){
            return i;
        }
    }
```



### 2.13 重置mock



```
    @Test
    public void reset_mock(){
        List list = mock(List.class);
        when(list.size()).thenReturn(10);
        list.add(1);
        assertEquals(10,list.size());
        //重置mock，清除所有的互动和预设
        reset(list);
        assertEquals(0,list.size());
    }
```



### 2.14 验证确切的调用次数



```
    @Test
    public void verifying_number_of_invocations(){
        List list = mock(List.class);
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(3);
        list.add(3);
        list.add(3);
        //验证是否被调用一次，等效于下面的times(1)
        verify(list).add(1);
        verify(list,times(1)).add(1);
        //验证是否被调用2次
        verify(list,times(2)).add(2);
        //验证是否被调用3次
        verify(list,times(3)).add(3);
        //验证是否从未被调用过
        verify(list,never()).add(4);
        //验证至少调用一次
        verify(list,atLeastOnce()).add(1);
        //验证至少调用2次
        verify(list,atLeast(2)).add(2);
        //验证至多调用3次
        verify(list,atMost(3)).add(3);
    }
```



### 2.15 连续调用



```
    @Test(expected = RuntimeException.class)
    public void consecutive_calls(){
        //模拟连续调用返回期望值，如果分开，则只有最后一个有效
        when(mockList.get(0)).thenReturn(0);
        when(mockList.get(0)).thenReturn(1);
        when(mockList.get(0)).thenReturn(2);
        when(mockList.get(1)).thenReturn(0).thenReturn(1).thenThrow(new RuntimeException());
        assertEquals(2,mockList.get(0));
        assertEquals(2,mockList.get(0));
        assertEquals(0,mockList.get(1));
        assertEquals(1,mockList.get(1));
        //第三次或更多调用都会抛出异常
        mockList.get(1);
    }
```



### 2.16 验证执行顺序



```
    @Test
    public void verification_in_order(){
        List list = mock(List.class);
        List list2 = mock(List.class);
        list.add(1);
        list2.add("hello");
        list.add(2);
        list2.add("world");
        //将需要排序的mock对象放入InOrder
        InOrder inOrder = inOrder(list,list2);
        //下面的代码不能颠倒顺序，验证执行顺序
        inOrder.verify(list).add(1);
        inOrder.verify(list2).add("hello");
        inOrder.verify(list).add(2);
        inOrder.verify(list2).add("world");
    }
```



### 2.17 确保模拟对象上无互动发生



```
    @Test
    public void verify_interaction(){
        List list = mock(List.class);
        List list2 = mock(List.class);
        List list3 = mock(List.class);
        list.add(1);
        verify(list).add(1);
        verify(list,never()).add(2);
        //验证零互动行为
        verifyZeroInteractions(list2,list3);
    }
```



### 2.18 找出冗余的互动(即未被验证到的)



```
    @Test(expected = NoInteractionsWanted.class)
    public void find_redundant_interaction(){
        List list = mock(List.class);
        list.add(1);
        list.add(2);
        verify(list,times(2)).add(anyInt());
        //检查是否有未被验证的互动行为，因为add(1)和add(2)都会被上面的anyInt()验证到，所以下面的代码会通过
        verifyNoMoreInteractions(list);

        List list2 = mock(List.class);
        list2.add(1);
        list2.add(2);
        verify(list2).add(1);
        //检查是否有未被验证的互动行为，因为add(2)没有被验证，所以下面的代码会失败抛出异常
        verifyNoMoreInteractions(list2);
    }
```



## 3 Mockito如何实现Mock[3]



Mockito并不是创建一个真实的对象，而是模拟这个对象，他用简单的when(mock.method(params)).thenRetrun(result)语句设置mock对象的行为，如下语句：

```
// 设置mock对象的行为 － 当调用其get方法获取第0个元素时，返回"first"
Mockito.when(mockedList.get(0)).thenReturn("first");
```

在Mock对象的时候，创建一个proxy对象，保存被调用的方法名（get），以及调用时候传递的参数（0），然后在调用thenReturn方法时再把“first”保存起来，这样，就有了构建一个stub方法所需的所有信息，构建一个stub。当get方法被调用的时候，实际上调用的是之前保存的proxy对象的get方法，返回之前保存的数据。

## 参考

[1] [单元测试之Stub和Mock](http://www.cnblogs.com/TankXiao/archive/2012/03/06/2366073.html#diff)

[2] [mockito简单教程](http://blog.csdn.net/sdyy321/article/details/38757135/)

[3] [Mockito入门](http://blog.csdn.net/jhg19900321/article/details/49531983)

[4] [学习Mockito](http://wenku.baidu.com/view/8def451a227916888486d73f.html)

[5] [Mockito 详解（一）基本用法](http://liangfei.me/2017/07/06/mockito-details-1-usage/)

[6] [Java测试框架系列：Mockito 详解：第一部分：对象创建] (https://juejin.cn/post/6976306102774267935)

[7] [单元测试 - Mockito 详解] (https://pdai.tech/md/develop/ut/dev-ut-x-mockito.html)