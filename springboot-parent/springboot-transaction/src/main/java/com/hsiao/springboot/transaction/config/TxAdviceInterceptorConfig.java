package com.hsiao.springboot.transaction.config;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 *
 * 1、Transactional注解事务
 *
 * 需要在进行事物管理的方法上添加注解@Transactional，或者偷懒的话直接在类上面添加该注解，使得所有的方法都进行事物的管理，但是依然需要在需要事务管理的类上都添加，工作量比较大，这里只是简单说下，具体的可以google或者bing
 *
 * 2、注解声明式事务
 * Component或Configuration中bean的区别
 *
 *
 * a.方式1，这里使用Component或Configuration事务都可以生效
 *
 * 创建Advice或Advisor
 * 4、使用tx标签配置的拦截器(AOP)
 *
 * @projectName springboot-parent
 * @title: TxAdviceInterceptorConfig
 * @description: TODO
 * @author xiao
 * @create 2021/2/27
 * @since 1.0.0
 */
@Aspect
//@Component 事务依然生效
@Configuration
public class TxAdviceInterceptorConfig {


    private static final int TX_METHOD_TIMEOUT = 5000;
    private static final String AOP_POINTCUT_EXPRESSION = "execution (* com.hsiao.springboot.transaction.service.impl.TransactionWithAopImpl.*(..))";

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        /*只读事务，不做更新操作*/
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        /*当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务*/
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        requiredTx.setRollbackRules(
                Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        requiredTx.setTimeout(TX_METHOD_TIMEOUT);
        Map<String, TransactionAttribute> txMap = new HashMap<>();
        txMap.put("add*", requiredTx);
        txMap.put("save*", requiredTx);
        txMap.put("insert*", requiredTx);
        txMap.put("update*", requiredTx);
        txMap.put("delete*", requiredTx);
        txMap.put("get*", readOnlyTx);
        txMap.put("query*", readOnlyTx);
        source.setNameMap(txMap);
        TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, source);
        return txAdvice;
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
        //return new DefaultPointcutAdvisor(pointcut, txAdvice);
    }
}


