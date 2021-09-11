package com.hsiao.springboot.async.config;


import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * 异步线程池配置
 *
 * @Async注解异步框架提供多种线程：
 *
 * SimpleAsyncTaskExecutor:不是真的线程池，这个类不重用线程，每次调用都会创建一个新的线程。
 *
 * SyncTaskExecutor:这个类没有实现异步调用，只是一个同步操作。只适用于不需要多线程的地方。
 *
 * ConcurrentTaskExecutor: Executor的适配类，不推荐使用。如果ThreadPoolTaskExecutor不满足要求时，才用考虑使用这个类。
 *
 * ThreadPoolTaskScheduler:可以使用cron表达式。
 *
 * ThreadPoolTaskExecutor : 最常使用，【推荐】。其实质是对java.util.concurrent.ThreadPoolExecutor的包装。
 *
 * 核心线程数10：线程池创建时候初始化的线程数
 * 最大线程数20：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
 * 缓冲队列200：用来缓冲执行任务的队列
 * 允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
 * 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
 * 线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
 * @projectName springboot-parent
 * @title: AsyncConfig
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    /** 核心线程数（默认线程数） */
    private static final int CORE_POOL_SIZE = 5;
    /** 最大线程数 */
    private static final int MAX_POOL_SIZE = 10;
    /** 允许线程空闲时间（单位：默认为秒） */
    private static final int KEEP_ALIVE_TIME = 10;
    /** 缓冲队列大小 */
    private static final int QUEUE_CAPACITY = 200;
    /** 线程池名前缀 */
    private static final String THREAD_NAME_PREFIX = "Async-Service-";

    /**
     * 默认情况下，在创建了线程池后，线程池中的线程数为0， 当有任务来之后，就会创建一个线程去执行任务，
     * 当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
     * 当队列满了，就继续创建线程，当线程数量大于等于maxPoolSize后，开始使用拒绝策略拒绝
     * @return
     */
    @Bean("taskExecutor") // bean的名称，默认为首字母小写的方法名
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);

        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        // 当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
        /**
         *当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize， 如果还有任务到來就会采取任务拒绝策略
         *通常有以下四种策略:
         *ThreadPoolExecutor.AbortPolicy :丟弃任务并抛出RejectedExecutionException异常。
         *ThreadPoolExecutor.DiscardPolicy:也是丢弃任务，但是不抛出异常。
         *ThreadPoolExecutor.DiscardOldestPolicy:丢弃队列最前面的任务，然后重新尝试执行任务(重复此过程).
         *ThreadPoolExecutor.CallerRunsPolicy: 重试添加当前的任务，自动重复调用execute()方法，直到成功.
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }


    /**
     * 解决ThreadPoolTaskScheduler线程池的优雅关闭
     *
     * Spring的ThreadPoolTaskScheduler为我们提供了相关的配置，只需要加入如下设置即可：
     * 说明：setWaitForTasksToCompleteOnShutdown（true）该方法就是这里的关键，用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean，这样这些异步任务的销毁就会先于Redis线程池的销毁。同时，这里还设置了setAwaitTerminationSeconds(60)，该方法用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
     * @return
     */
 /*   @Bean("taskExecutorClose")
    public Executor taskExecutorClose() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(20);
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        return executor;
    }*/


    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.error("Error Occurs in async method:{}", ex.getMessage());
    }
}

