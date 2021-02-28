package com.hsiao.springboot.transaction;


import com.hsiao.springboot.transaction.service.NotEffectService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * 不生效的demo用例
 *
 * @projectName springboot-parent
 * @title: NotEffectTest
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
public class NotEffectTest {

    @Autowired
    private NotEffectService notEffectService;

    /**
     * 不生效的几种姿势：
     * - 私有方法上，不生效
     * - 内部调用，不生效
     * - 未抛运行异常，不生效
     * - 子线程处理任务，某个线程执行异常，不生效
     */
    @Test
    public void testNotEffect() {
        testCall(520, (id) -> notEffectService.testCompleException(520));

        testCall(530, (id) -> notEffectService.testCall(530));

        testCall(540, (id) -> notEffectService.testMultThread(540));

        testCall(550, (id) -> notEffectService.testMultThread2(550));
    }

    private void testCall(int id, CallFunc<Integer, Boolean> func) {
        System.out.println("============ 事务不生效case start ========== ");
        notEffectService.query("transaction before", id);
        try {
            // 事务可以正常工作
            func.apply(id);
        } catch (Exception e) {
        }
        notEffectService.query("transaction end", id);
        System.out.println("============ 事务不生效case end ========== \n");
    }

    @FunctionalInterface
    public interface CallFunc<T, R> {
        R apply(T t) throws Exception;
    }

}

