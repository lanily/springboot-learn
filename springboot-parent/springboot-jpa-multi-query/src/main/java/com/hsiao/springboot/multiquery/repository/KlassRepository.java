package com.hsiao.springboot.multiquery.repository;

import com.hsiao.springboot.multiquery.entity.Klass;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author hsiao
 */
public interface KlassRepository extends CrudRepository<Klass, Long>, JpaSpecificationExecutor {
}
