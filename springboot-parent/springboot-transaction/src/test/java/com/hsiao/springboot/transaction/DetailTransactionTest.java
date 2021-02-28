package com.hsiao.springboot.transaction;


import com.hsiao.springboot.transaction.service.IsolationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: DetailTransactionTest
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
public class DetailTransactionTest {

    @Autowired
    private IsolationService isolationTransactionService;

    @Test
    public void testIsolation() throws InterruptedException {
        testRuIsolation();
        Thread.sleep(2000);

        testRcIsolation();
        Thread.sleep(2000);

        testReadOnlyCase();
        Thread.sleep(3000);

        testSerializeIsolation();
        Thread.sleep(2000);
    }


    /**
     * rr
     * 测试只读事务
     */
    private void testReadOnlyCase() throws InterruptedException {
        // 子线程开启只读事务，主线程执行修改
        int id = 320;
        new Thread(new Runnable() {
            @Override
            public void run() {
                call("rr 只读事务 - read", id, isolationTransactionService::readRrTransaction);
            }
        }).start();

        Thread.sleep(1000);

        call("rr 读写事务", id, isolationTransactionService::rrTransaction);
    }


    /**
     * ru 隔离级别
     */
    private void testRuIsolation() throws InterruptedException {
        int id = 330;
        new Thread(new Runnable() {
            @Override
            public void run() {
                call("ru: 只读事务 - read", id, isolationTransactionService::readRuTransaction);
            }
        }).start();

        call("ru 读写事务", id, isolationTransactionService::ruTransaction);
    }

    /**
     * rc 隔离级别
     */
    private void testRcIsolation() throws InterruptedException {
        int id = 340;
        new Thread(new Runnable() {
            @Override
            public void run() {
                call("rc: 只读事务 - read", id, isolationTransactionService::readRcTransaction);
            }
        }).start();

        Thread.sleep(1000);

        call("rc 读写事务 - read", id, isolationTransactionService::rcTranaction);
    }


    /**
     * Serialize 隔离级别
     */
    private void testSerializeIsolation() throws InterruptedException {
        int id = 350;
        new Thread(new Runnable() {
            @Override
            public void run() {
                call("Serialize: 只读事务 - read", id, isolationTransactionService::readSerializeTransaction);
            }
        }).start();

        Thread.sleep(1000);

        call("Serialize 读写事务 - read", id, isolationTransactionService::serializeTransaction);
    }


    private void call(String tag, int id, CallFunc<Integer, Boolean> func) {
        System.out.println("============ " + tag + " start ========== ");
        try {
            func.apply(id);
        } catch (Exception e) {
        }
        System.out.println("============ " + tag + " end ========== \n");
    }


    @FunctionalInterface
    public interface CallFunc<T, R> {
        R apply(T t) throws Exception;
    }

}

