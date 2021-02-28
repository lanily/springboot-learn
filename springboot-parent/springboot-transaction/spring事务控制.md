# Spring声明式事务

## 基于XML

- 步骤：

	1. 配置事务管理器
	2. 配置事务通知
		- 此时需要导入tx和aop的约束
		- 使用`tx:advice`标签配置事务通知
			- 属性：
				- `id`：给事务通知起一个唯一标识
				- `transaction-manager`：给事务通知提供一个事务管理器
	3. 配置AOP的通用切入点表达式
	4. 建立事务通知和切入点表达式的关系 `aop:advisor`
	5. 配置事物的属性（在事务的通知`tx:advisor`标签的内部）
		- `isolation`：用于指定事务的隔离级别，默认值式DEFAULT，表示使用数据库的默认隔离级别            
		- `propagation`：指定事务的传播行为，默认值为REQUIRED，表示一定会有事务，增删改的选择，。查询方法可以使用SUPPORTS           
		- `read-only`：用于指定事务是否只读。只有查询方法才能设置为true。默认值false，表示可读写           
		- `timeout`：指定事务的超时事件，默认值为-1，表示永不超            
		- `rollback-for`：用于指定一个异常，当产生该异常时，事务回滚，产生其他异常时，事务不回滚。没有默认值，表示任何异常都回滚            
		- `no-rollback-for`：用于指定一个异常，当产生该异常时事务不回滚，产生其他异常时回滚。没有默认值，表示任何异常都回滚

- ```xml
	<!-- 配置事务管理器 -->
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource"/>
	</bean>
	
	<!-- 配置事务通知 -->
	<tx:advice id="txAdvice" transaction-manager="transactionManager">
		<!--配置事务的属性-->
		<tx:attributes>
			<tx:method name="transfer" propagation="REQUIRED"/>
			<tx:method name="find*" propagation="SUPPORTS"/>
		</tx:attributes>
	</tx:advice>
	
	<!--配置AOP-->
	<aop:config>
		<aop:pointcut id="pt1" expression="execution(* com.ce.service.impl.*.*(..))"/>
		<!--建立切入点表达式和事务通知的对应关系-->
		<aop:advisor advice-ref="txAdvice" pointcut-ref="pt1"/>
	</aop:config>
	```

## 基于注解

- 步骤：

	1. 配置事务管理器
	2. 开启spring对注解事务的支持 `tx:annotation-driven`
	3. 在需要事务支持的地方使用`@Transactional`注解

- ```xml
	<!-- 配置事务管理器 -->
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource"/>
	</bean>
	
	<!--开启spring对注解事务的支持-->
	<tx:annotation-driven transaction-manager="transactionManager"/>
	```

- ```java
	/**
	 * 账户的业务层实现类
	 */
	@Service("accountService")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) //只读型事务的配置
	public class AccountServiceImpl implements AccountService {
	    @Autowired
	    private AccountDao accountDao;
	
	    /**
	     * 根据id查找
	     *
	     * @param id
	     */
	    @Override
	    public Account findAccountById(Integer id) {
	        return accountDao.findAccountById(id);
	    }
	
	
	    /**
	     * 转账
	     *
	     * @param sourceName 转出账户名称
	     * @param targetName 转入账户名称
	     * @param money      转账金额
	     */
	    @Transactional(propagation = Propagation.REQUIRED, readOnly = false) //事务的配置
	    @Override
	    public void transfer(String sourceName, String targetName, Float money) {
	        //1.根据名称查询转出账户
	        Account source = accountDao.findAccountByName(sourceName);
	        //2.根据名称查询转入账户
	        Account target = accountDao.findAccountByName(targetName);
	        //3.转出账户减钱
	        source.setMoney(source.getMoney() - money);
	        //4.转入账户加钱
	        target.setMoney(target.getMoney() + money);
	        //5.更新转出账户
	        accountDao.updateAccount(source);
	        int i = 1 / 0;
	        //6.更新转入账户
	        accountDao.updateAccount(target);
	    }
	}
	```

## 基于注解（无XML文件）

- 总配置类

	```java
	/**
	 * spring的配置类，相当于bean.xml
	 */
	@Configuration
	@ComponentScan("com.ce")
	@Import({JdbcConfig.class, TransactionConfig.class})
	@EnableTransactionManagement //开启事务注解支持
	public class SpringConfiguration {}
	```

- 数据库连接配置类

	```java
	/**
	 * 和连接数据库相关的配置
	 */
	@Configuration
	@PropertySource("classpath:JdbcConfig.properties")
	public class JdbcConfig {
	    @Value("${jdbc.driver}")
	    private String driver;
	    @Value("${jdbc.url}")
	    private String url;
	    @Value("${jdbc.username}")
	    private String username;
	    @Value("${jdbc.password}")
	    private String password;
	
	    /**
	     * 创建JdbcTEmplate对象
	     */
	    @Bean(name = "jdbcTemplate")
	    public JdbcTemplate createJdbcTemplate(DataSource dataSource) {
	        return new JdbcTemplate(dataSource);
	    }
	
	    @Bean(name = "dataSource")
	    public DataSource createDataSource() {
	        DriverManagerDataSource ds = new DriverManagerDataSource();
	        ds.setDriverClassName(driver);
	        ds.setUrl(url);
	        ds.setUsername(username);
	        ds.setPassword(password);
	        return ds;
	    }
	}
	```

- 事务相关的配置类

	```java
	/**
	 * 和事务相关的配置类
	 */
	@Configuration
	public class TransactionConfig {
	    /**
	     * 用于创建事务管理器对象
	     */
	    @Bean(name = "transactionManager")
	    public PlatformTransactionManager createTransactionManager(DataSource dataSource) {
	        return new DataSourceTransactionManager(dataSource);
	    }
	}
	```

- ```java
	/**
	 * 账户的业务层实现类
	 */
	@Service("accountService")
	@Transactional(transactionManager = "transactionManager",propagation = Propagation.SUPPORTS,readOnly = true)
	public class AccountServiceImpl implements AccountService {
	    @Autowired
	    private AccountDao accountDao;
	
	    /**
	     * 根据id查找
	     *
	     * @param id
	     */
	    @Override
	    public Account findAccountById(Integer id) {
	        return accountDao.findAccountById(id);
	    }
	
	    /**
	     * 转账
	     *
	     * @param sourceName 转出账户名称
	     * @param targetName 转入账户名称
	     * @param money      转账金额
	     */
	    @Transactional(transactionManager = "transactionManager",propagation = Propagation.REQUIRED,readOnly = false)
	    @Override
	    public void transfer(String sourceName, String targetName, Float money) {
	        //1.根据名称查询转出账户
	        Account source = accountDao.findAccountByName(sourceName);
	        //2.根据名称查询转入账户
	        Account target = accountDao.findAccountByName(targetName);
	        //3.转出账户减钱
	        source.setMoney(source.getMoney() - money);
	        //4.转入账户加钱
	        target.setMoney(target.getMoney() + money);
	        //5.更新转出账户
	        accountDao.updateAccount(source);
	        // int i=1/0;
	        //6.更新转入账户
	        accountDao.updateAccount(target);
	    }
	}
	```

## 编程式事务（了解）

- ```xml
	<!-- spring编程式事务控制 -->
	
	<!--配置事务管理器-->
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource"/>
	</bean>
	
	<!-- 事务模板对象-->
	<bean id="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate">
		<property name="transactionManager" ref="transactionManager"/>
	</bean>
	
	```

- ```java
	@Service("accountService")
	public class AccountServiceImpl implements AccountService {
	    @Autowired
	    private AccountDao accountDao;
	    @Autowired
	    private TransactionTemplate transactionTemplate;
	
	    /**
	     * 根据id查找
	     *
	     * @param id
	     */
	    @Override
	    public Account findAccountById(Integer id) {
	        return transactionTemplate.execute(new TransactionCallback<Account>() {
	            @Override
	            public Account doInTransaction(TransactionStatus transactionStatus) {
	                
	                return accountDao.findAccountById(id);
	                
	            }
	        });
	    }
	
	
	    /**
	     * 转账
	     *
	     * @param sourceName 转出账户名称
	     * @param targetName 转入账户名称
	     * @param money      转账金额
	     */
	    @Override
	    public void transfer(String sourceName, String targetName, Float money) {
	        transactionTemplate.execute(new TransactionCallback<Object>() {
	            @Override
	            public Object doInTransaction(TransactionStatus transactionStatus) {
	                
	                //1.根据名称查询转出账户
	                Account source = accountDao.findAccountByName(sourceName);
	                //2.根据名称查询转入账户
	                Account target = accountDao.findAccountByName(targetName);
	                //3.转出账户减钱
	                source.setMoney(source.getMoney() - money);
	                //4.转入账户加钱
	                target.setMoney(target.getMoney() + money);
	                //5.更新转出账户
	                accountDao.updateAccount(source);
	                // int i = 1 / 0;
	                //6.更新转入账户
	                accountDao.updateAccount(target);
	                return null;
	                
	            }
	        });
	
	    }
	}
	```

总结：

1.在需要事务管理的地方加@Transactional 注解。@Transactional 注解可以被应用于接口定义和接口方法、
类定义和类的 public 方法上。

2.@Transactional 注解只能应用到 public 可见度的方法上。 如果你在 protected、private 或者
package-visible 的方法上使用 @Transactional 注解，它也不会报错， 但是这个被注解的方法将不会展示
已配置的事务设置。

3.注意仅仅 @Transactional 注解的出现不足于开启事务行为，它仅仅 是一种元数据。
必须在配置文件中使用配置元素，才真正开启了事务行为。(spring配置文件中,开启声明式事务)
例如可以这么配置:
  <!--======= 事务配置 Begin ================= -->
	<!-- 事务管理器（由Spring管理MyBatis的事务） -->
	<bean id="transactionManager"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<!-- 关联数据源 -->
		<property name="dataSource" ref="dataSource"></property>
	</bean>
	<tx:annotation-driven transaction-manager="transactionManager"
		proxy-target-class="true" />
  <!--======= 事务配置 End =================== -->

4.通过 元素的 “proxy-target-class” 属性值来控制是基于接口的还是基于类的代理被创建。
如果 “proxy-target-class” 属值被设置为 “true”，那么基于类的代理将起作用（这时需要CGLIB库
cglib.jar在CLASSPATH中）。如果 “proxy-target-class” 属值被设置为 “false” 或者这个属性被省略，
那么标准的JDK基于接口的代理将起作用。

5.Spring团队建议在具体的类（或类的方法）上使用 @Transactional 注解，而不要使用在类所要实现的任何接口
上。在接口上使用 @Transactional 注解，只能当你设置了基于接口的代理时它才生效。因为注解是 不能继承
的，这就意味着如果正在使用基于类的代理时，那么事务的设置将不能被基于类的代理所识别，而且对象也将不会被事
务代理所包装。

6.@Transactional的事务开启 ，或者是基于接口的 或者是基于类的代理被创建。所以在同一个类中一个无事务的方法调用另一个有事务的方法，事务是不会起作用的。如果在有事务的方法中调用另外一个有事务的方法，那么事务会起作用，并且共用事务。如果在有事务的方法中调用另外一个没有事务的方法，那么事务也会起作用。

不生效的原因：
当从类外调用没有添加事务的方法a()时，从spring容器获取到的serviceImpl对象实际是包装好的proxy对象，因此调用a()方法的对象是动态代理对象。而在类内部a()调用b()的过程中，实质执行的代码是this.b()，此处this对象是实际的serviceImpl对象而不是本该生成的代理对象，因此直接调用了b()方法。

解决办法：

1. 放到不同的类中进行调用
2. 在spring配置文件中加入配置，
  <!-- 激活自动代理功能 -->
   <aop:aspectj-autoproxy/>
   <aop:aspectj-autoproxy proxy-target-class=“true” expose-proxy=“true” />
3. 将之前使用普通调用的方法,换成使用代理调用，那怎么获取代理对象呢?
   + 使用 AopContext.currentProxy() 获取代理对象,但是需要配置exposeProxy=true
     ((TestService)AopContext.currentProxy()).testTransactional2();
   获取到TestService的代理类，再调用事务方法，强行经过代理类，激活事务切面。
   + 使用 ApplicationContext 上下文对象获取该对象;
   + springboot启动类加上注解:@EnableAspectJAutoProxy(exposeProxy = true)
4. 使用异步操作，另外开启一个线程或者将这个消息写入到队列里面，在其他的地方进行处理


