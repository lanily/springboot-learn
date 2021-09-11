package com.hsiao.springboot.multiquery.repository;

import com.hsiao.springboot.multiquery.entity.Teacher;
import org.springframework.data.repository.CrudRepository;

/**
 * @author hsiao
 */
public interface TeacherRepository extends CrudRepository<Teacher, Long> {
    Teacher findFirstByNameOrderByBeginCreateTimeDesc(String name);
}