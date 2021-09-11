package com.hsiao.springboot.inject.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: CacheServiceImpl
 * @description: TODO
 * @author xiao
 * @create 2021/5/30
 * @since 1.0.0
 */
@Service
@Slf4j
public class CacheServiceImpl implements CacheService{

    @Override
    public void cache() {
        log.info("<<========== call cache service ==========>>");
    }
}

