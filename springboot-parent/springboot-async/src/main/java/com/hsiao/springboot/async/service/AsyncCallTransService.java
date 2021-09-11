package com.hsiao.springboot.async.service;


import com.hsiao.springboot.async.dao.TransactionTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: AsyncCallTransService
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@Service
public class AsyncCallTransService {

    @Autowired
    TransactionTask transactionTask;

    @Async
    public void callTransTask () {
        transactionTask.inMoney("async", 100d);
    }
}

