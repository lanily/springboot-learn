package com.hsiao.springboot.async.service;


import com.hsiao.springboot.async.dao.AsyncDaoTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: AsyncService
 * @description: TODO
 * @author xiao
 * @create 2021/3/6
 * @since 1.0.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TransCallAsyncService {

    @Autowired
    AsyncDaoTask asyncDaoTask;

    public void callAsyncTask() {
        asyncDaoTask.outMoney("async", 50d);
        int i = 1/0;
    }

}

