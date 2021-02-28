package com.hsiao.springboot.transaction.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * 事务传递 示例demo
 *
 * @projectName springboot-parent
 * @title: PropagationService
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
@Service
public class PropagationToService {

    @Autowired
    private PropagationFromService propagationFromService;

    @Transactional(rollbackFor = Exception.class)
    public void support(int id) throws Exception {
        // 事务运行
        propagationFromService.support(id);
    }


    @Transactional(rollbackFor = Exception.class)
    public void notSupport(int id) throws Exception {
        // 挂起当前事务，以非事务方式运行
        try {
            propagationFromService.notSupport(id);
        } catch (Exception e) {
        }

        propagationFromService.query("notSupportCall: ", id);
        propagationFromService.updateName(id, "外部更新");
        propagationFromService.query("notSupportCall: ", id);
        throw new Exception("回滚");
    }

    @Transactional(rollbackFor = Exception.class)
    public void never(int id) throws Exception {
        propagationFromService.never(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void nested(int id) throws Exception {
        propagationFromService.updateName(id, "外部事务修改");
        propagationFromService.query("nestedCall: ", id);
        try {
            propagationFromService.nested(id);
        } catch (Exception e) {
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void nested2(int id) throws Exception {
        // 嵌套事务，外部回滚，会同步回滚内部事务
        propagationFromService.updateName(id, "外部事务修改");
        propagationFromService.query("nestedCall: ", id);
        propagationFromService.nested2(id);
        throw new Exception("事务回滚");
    }

}

