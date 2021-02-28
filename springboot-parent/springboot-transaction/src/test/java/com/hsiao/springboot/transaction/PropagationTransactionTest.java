package com.hsiao.springboot.transaction;


import com.hsiao.springboot.transaction.service.PropagationToService;
import com.hsiao.springboot.transaction.service.PropagationFromService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: PropagationTransactionTest
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
public class PropagationTransactionTest {

    @Autowired
    private PropagationFromService propagationFromService;

    @Autowired
    PropagationToService propagationToService;

    public void testPropagation() {
        testRequired();

        testSupport();
        testMandatory();
        testNotSupport();
        testNever();
        testNested();
    }

    private void testRequired() {
        int id = 420;
        call("Required事务运行", id, propagationFromService::required);
    }

    private void testSupport() {
        int id = 430;
        // 非事务方式，异常不会回滚
        call("support无事务运行", id, propagationFromService::support);

        // 事务运行
        id = 440;
        call("support事务运行", id, propagationToService::support);
    }

    private void testMandatory() {
        int id = 450;
        // 非事务方式，抛异常，这个必须在一个事务内部执行
        call("mandatory非事务运行", id, propagationFromService::mandatory);
    }


    private void testNotSupport() {
        int id = 460;
        call("notSupport", id, propagationToService::notSupport);
    }


    private void testNever() {
        int id = 470;
        call("never非事务", id, propagationToService::never);
    }

    private void testNested() {
        int id = 480;
        call("nested事务", id, propagationToService::nested);

        id = 490;
        call("nested事务2", id, propagationToService::nested2);
    }

    private void call(String tag, int id, CallFunc<Integer> func) {
        System.out.println("============ " + tag + " start ========== ");
        propagationFromService.query(tag, id);
        try {
            func.apply(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        propagationFromService.query(tag, id);
        System.out.println("============ " + tag + " end ========== \n");
    }


    @FunctionalInterface
    public interface CallFunc<T> {

        void apply(T t) throws Exception;
    }

}

