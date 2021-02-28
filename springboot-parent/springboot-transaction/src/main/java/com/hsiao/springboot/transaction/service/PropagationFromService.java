package com.hsiao.springboot.transaction.service;


import com.hsiao.springboot.transaction.dao.impl.PropagationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * 事务传播机制测试
 *
 * @projectName springboot-parent
 * @title: PropagationTransactionService
 * @description: TODO
 * @author xiao
 * @create 2021/2/28
 * @since 1.0.0
 */
@Service
public class PropagationFromService {


    @Autowired
    PropagationDao propagationDao;

    /**
     * 如果存在一个事务，则支持当前事务。如果没有事务则开启一个新的事务
     *
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void required(int id) throws Exception {
        if (propagationDao.updateName(id)) {
            propagationDao.query("required: after updateMoney name", id);
            if (propagationDao.updateMoney(id)) {
                return;
            }
        }

        throw new Exception("事务回滚!!!");
    }


    /**
     * 如果存在一个事务，支持当前事务。如果没有事务，则非事务的执行
     *
     * 简单来讲，外部直接调用这个方法时，非事务方式执行；通过supportTransaction 这个事务方法内部调用support方法时，以事务方式执行
     *
     * @param id
     * @throws Exception
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public void support(int id) throws Exception {
        if (propagationDao.updateName(id)) {
            propagationDao.query("support: after updateMoney name", id);
            if (propagationDao.updateMoney(id)) {
                return;
            }
        }

        throw new Exception("事务回滚!!!");
    }


    /**
     * 需要在一个正常的事务内执行，否则抛异常
     *
     * @param id
     * @throws Exception
     */
    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
    public void mandatory(int id) throws Exception {
        if (propagationDao.updateName(id)) {
            propagationDao.query("mandatory: after updateMoney name", id);
            if (propagationDao.updateMoney(id)) {
                return;
            }
        }

        throw new Exception("事务回滚!!!");
    }


    /**
     * 总是非事务地执行，并挂起任何存在的事务
     *
     * @param id
     * @throws Exception
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    public void notSupport(int id) throws Exception {
        if (propagationDao.updateName(id)) {
            propagationDao.query("notSupport: after updateMoney name", id);
            if (propagationDao.updateMoney(id)) {
                return;
            }
        }
        throw new Exception("回滚!");
    }

    /**
     * 总是非事务地执行，如果存在一个活动事务，则抛出异常。
     *
     * @param id
     * @throws Exception
     */
    @Transactional(propagation = Propagation.NEVER, rollbackFor = Exception.class)
    public void never(int id) throws Exception {
        if (propagationDao.updateName(id)) {
            propagationDao.query("notSupport: after updateMoney name", id);
            if (propagationDao.updateMoney(id)) {
                return;
            }
        }
    }


    /**
     * 如果不存在事务，则开启一个事务运行
     *
     * 如果存在事务，则运行一个嵌套事务；
     *
     * 嵌套事务：外部事务回滚，内部事务也会被回滚；内部事务异常，外部无问题，并不会回滚外部事务
     *
     * @param id
     * @throws Exception
     */
    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public void nested(int id) throws Exception {
        if (propagationDao.updateName(id)) {
            propagationDao.query("nested: after updateMoney name", id);
            if (propagationDao.updateMoney(id)) {
                return;
            }
        }

        throw new Exception("事务回滚!!!");
    }


    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public void nested2(int id) throws Exception {
        if (propagationDao.updateName(id)) {
            propagationDao.query("nested: after updateMoney name", id);
            if (propagationDao.updateMoney(id)) {
                return;
            }
        }
    }

    public void query(String tag, int id) {
        propagationDao.query(tag, id);
    }

    public void updateName(int id, String name) {
        propagationDao.updateName(id, name);
    }
}

