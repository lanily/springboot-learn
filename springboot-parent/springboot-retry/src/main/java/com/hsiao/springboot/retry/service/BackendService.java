package com.hsiao.springboot.retry.service;


import com.hsiao.springboot.retry.exception.BackendNotAvailableException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: BackendService
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
public interface BackendService {

    /**
     * 说明：
     *
     * @Retryable：这个注释是说，如果我们的接口方法抛出RemoteServiceNotAvailableException 异常，则在返回响应之前最多重试3次，同时，每次重试都会间隔1秒的延迟。
     * @Recover：如果达到最大重试次数后，依然出现异常，则使用此方法返回响应。
     * 前面说过，在调用后台服务的实际方法中，我们是通过添加自定义开关参数来控制Exception的。
     *
     * 代码逻辑很简单，只要满足条件就返回期望的异常并触发充实，否则返回成功响应。
     *
     * 另外，我们还基于“随机数”添加了一些随机逻辑，以模拟故障的随机性。
     * @param retry
     * @param fallback
     * @return
     */
    @Retryable(value = {BackendNotAvailableException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000))
    String service(boolean retry, boolean fallback);

    @Recover
    String serviceFallback(BackendNotAvailableException e);
}

