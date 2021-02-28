package com.hsiao.springboot.transaction.service;

public interface RollBackService {

    void rollback() throws InterruptedException;

    void noRollback() throws InterruptedException;

    void rollbackAuto() throws Exception;

    void rollbackManual();

    void rollbackPart();

    void defaultRollback();

    void rollbackFor1() throws InterruptedException;

    void rollbackFor2() throws InterruptedException;

    void rollbackFor3() throws InterruptedException;

    void nonRollback1() throws InterruptedException;

    void nonRollback2() throws InterruptedException;
}
