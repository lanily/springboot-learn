package com.hsiao.springboot.transaction.config;


import com.hsiao.springboot.transaction.service.AccountService;
import java.util.Properties;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.interceptor.TransactionProxyFactoryBean;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: BeanConfig
 * @description: TODO
 * @author xiao
 * @create 2021/2/27
 * @since 1.0.0
 */
@Order(2)
@Configuration
//@Import(DataSourceConfig.class)
public class ApplicationContextConfig {

    @Resource(name = "transactionWithSingleTxProxy")
    AccountService transactionWithSingleTxProxy;

    @Resource(name = "transactionWithSharedTxProxy")
    AccountService transactionWithSharedTxProxy;

    @Autowired DataSource dataSource;

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        transactionManager.setDefaultTimeout(3000);
        return transactionManager;
//        return new DataSourceTransactionManager(dataSource());
    }


    /**
     * 1、每个Bean都有一个代理（基于TransactionProxyFactoryBean代理）
     * @return
     */
    @Bean(name = "transactionWithSingleTxProxyBean")
    public TransactionProxyFactoryBean transactionWithSingleTxProxyBean() {
        TransactionProxyFactoryBean transactionWithSingleTxProxyBean = new TransactionProxyFactoryBean();
//        配置事务管理器
        transactionWithSingleTxProxyBean.setTransactionManager(transactionManager());
        transactionWithSingleTxProxyBean.setTarget(transactionWithSingleTxProxy);
        transactionWithSingleTxProxyBean.setProxyInterfaces(new Class[]{AccountService.class});
//        配置事务属性
        Properties properties = new Properties();
        properties.put("*", "PROPAGATION_REQUIRED");
        transactionWithSingleTxProxyBean.setTransactionAttributes(properties);

        return transactionWithSingleTxProxyBean;
    }

    /**
     *
     * 2、所有Bean共享一个代理基类（基于TransactionProxyFactoryBean代理）
     * @return
     */
//    @Bean(name = "transactionBase")
//    @Lazy(true)
    public TransactionProxyFactoryBean transactionBase() {
        TransactionProxyFactoryBean transactionBase = new TransactionProxyFactoryBean();
//        配置事务管理器
        transactionBase.setTransactionManager(transactionManager());
//        配置事务属性
        Properties properties = new Properties();
        properties.put("*", "PROPAGATION_REQUIRED");
        transactionBase.setTransactionAttributes(properties);
        return transactionBase;
    }

    @Bean(name = "transactionWithSharedTxProxyBean")
    public TransactionProxyFactoryBean transactionWithSharedTxProxyBean() {
        TransactionProxyFactoryBean transactionBase = transactionBase();
        transactionBase.setTarget(transactionWithSharedTxProxy);
        return transactionBase;
    }

    /**
     * 3、使用拦截器
     * @return
     */
    @Bean(name = "transactionInterceptors")
    public TransactionInterceptor transactionInterceptor() {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(transactionManager());
        // 配置事务属性
        Properties properties = new Properties();
        properties.put("*", "PROPAGATION_REQUIRED");
        transactionInterceptor.setTransactionAttributes(properties);

        return transactionInterceptor;
    }

    //使用BeanNameAutoProxyCreator来创建代理
    @Bean
    public BeanNameAutoProxyCreator beanNameAutoProxyCreator(){
        BeanNameAutoProxyCreator beanNameAutoProxyCreator=new BeanNameAutoProxyCreator();
        //设置要创建代理的那些Bean的名字
        beanNameAutoProxyCreator.setBeanNames("*InterceptorService");
        //设置拦截链名字(这些拦截器是有先后顺序的)
        beanNameAutoProxyCreator.setInterceptorNames("transactionInterceptors");
        return beanNameAutoProxyCreator;
    }

}