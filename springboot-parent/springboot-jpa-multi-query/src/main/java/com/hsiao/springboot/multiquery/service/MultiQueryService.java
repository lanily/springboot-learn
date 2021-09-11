package com.hsiao.springboot.multiquery.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author hsiao
 */
public interface MultiQueryService {
    /**
     * 通过传入的实体进行多条件查询
     * @param entity
     * @param pageable
     * @return
     * hsiao
     */
    Page<?> page(JpaSpecificationExecutor jpaSpecificationExecutor, Object entity, Pageable pageable);

    /**
     * 通过传入的实体查询所有数据
     * @param entity
     * @return
     * hsiao
     */
    List<?> findAll(JpaSpecificationExecutor jpaSpecificationExecutor, Object entity);
}
