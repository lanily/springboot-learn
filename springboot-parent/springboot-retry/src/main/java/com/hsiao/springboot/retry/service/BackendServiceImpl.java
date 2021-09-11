package com.hsiao.springboot.retry.service;


import com.hsiao.springboot.retry.exception.BackendNotAvailableException;
import java.util.Random;
import org.springframework.stereotype.Service;

/**
 *
 * 〈一句话功能简述〉<br>
 * @projectName springboot-parent
 * @title: BackendServiceImpl
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
@Service("backendService")
public class BackendServiceImpl implements BackendService {

    private static int count = 0;

    @Override
    public String service(boolean retry, boolean fallback) {
        if (retry) {
            System.out.println("模拟开关已打开，开始模拟异常......");

            if (fallback) {
                count++;
                System.out.println("模拟直接异常开关已打开，直接进行重试......" + count + "次");
                throw new BackendNotAvailableException(
                        "抛出异常，spring-retry进行重试！");
            }

            System.out.println("......模拟随机异常（是否整除2）......");
            int random = new Random().nextInt(4);

            System.out.println("随机数为 : " + random);
            if (random % 2 == 0) {
                count++;
                System.out.println("出现随机异常......进行重试......" + count + "次");
                throw new BackendNotAvailableException("抛出异常，spring-retry进行重试！");
            }
        }
        System.out.println("调用正常!");
        count = 0;
        return "Hello Service!";
    }

    @Override
    public String serviceFallback(BackendNotAvailableException e) {
        System.out.println("所有重试已完成, 调用serviceFallback方法!!!");
        count = 0;
        return "所有重试已完成, 调用serviceFallback方法!!!";
    }
}
