/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: EmployeeService Author:   xiao Date:     2020/10/24
 * 20:40 History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hsiao.springboot.ehcache.service;


import com.hsiao.springboot.ehcache.entity.Employee;
import com.hsiao.springboot.ehcache.exception.RecordNotFoundException;
import com.hsiao.springboot.ehcache.repository.EmployeeRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.cache.annotation.CacheResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Employee Service
 *
 * @projectName springboot-parent
 * @title: EmployeeService
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
@Service
public class EmployeeService {


    @CacheResult(cacheName="employee")
    public Employee getEmployee(int id) {
//        System.out.println("未从缓存读取 " + id);
        switch (id) {
            case 1:
                return new Employee(Long.valueOf(id), "Steve", "jobs");
            case 2:
                return new Employee(Long.valueOf(id), "bill", "gates");
            default:
                return new Employee(Long.valueOf(id), "unknown", "unknown");
        }
    }

    /** -------------jpa---------------------- */
    @Autowired
    EmployeeRepository employeeRepository;

    @Cacheable(value = "employeeCache", key = "#id")
    public Optional<Employee> getEmployee(Long id) {
        return employeeRepository.findById(id);
    }

    /**
     * 注意：如果没有指定key，则方法参数作为key保存到缓存中
     */
    /**
     * @param employee
     * @return
     * @CachePut缓存新增的或更新的数据到缓存，其中缓存的名称为employeeCache，数据的key是employee的id
     */
    @CachePut(value = "employeeCache", key = "#employee.id")
    @Transactional
    public Employee saveOrUpdateEmployee(Employee employee) {
        return employeeRepository.saveAndFlush(employee);
    }

    @Cacheable(value = "employeeCache", key = "#entity.id")
    public Employee createOrUpdateEmployee(Employee entity) throws RecordNotFoundException {
        Optional<Employee> employee =  employeeRepository.findById(entity.getId());
        if (employee.isPresent()) {
            Employee newEntity = employee.get();
            newEntity.setFirstName(entity.getFirstName());
            newEntity.setLastName(entity.getLastName());
            newEntity = employeeRepository.saveAndFlush(newEntity);
            return newEntity;
        } else {
            entity = employeeRepository.save(entity);
            employeeRepository.saveAndFlush(entity);
            return entity;
        }
    }

    /**
     * @param id
     * @CacheEvict从缓存employeeCache中删除key为id的数据
     */
    @CacheEvict(value = "employeeCache", key = "#id")
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @CacheEvict(value = "employeeCache", key = "#id")
    public void deleteEmployeeById(Long id) throws RecordNotFoundException {
        Optional<Employee> employee =  employeeRepository.findById(id);
        if (employee.isPresent()) {
            employeeRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No employee record exist for given id");
        }
    }

    @CacheEvict(value = "employeeCache", allEntries = true)
    public void deleteAllEmployees() {
        employeeRepository.deleteAll();
    }

    @CacheEvict(value = "employeeCache", beforeInvocation = true)
    public void deleteAllBeforeInvocation() {
        employeeRepository.deleteAll();
    }


    /**
     * @param id
     * @return
     * @Cacheable缓存key为employee的id数据到缓存employeeCache中
     */
    @Cacheable(value = "employeeCache", key = "#id")
    public Employee getEmployeeById(Long id) throws RecordNotFoundException {
        Optional<Employee> employee =  employeeRepository.findById(id);
        if (employee.isPresent()) {
            return employee.get();
        } else {
            throw new RecordNotFoundException("No employee record exist for given id");
        }
    }

    /**
     * no id  need to look into
     * @return
     */
    @Cacheable(value = "employeeCache", key = "#id")
    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = employeeRepository.findAll();
        if (employeeList.size() > 0) {
            return employeeList;
        }

        return new ArrayList<>();
    }

    /**
     * 分页查询
     *
     * @param pageSize 每页个数
     * @param pageNum 页码
     * @return
     */
    @RequestMapping(value = "findByPage", method = RequestMethod.GET)
    public Map<String, Object> findByPage(Integer pageSize, Integer pageNum) {
        Map<String, Object> map = new HashMap();
        Page<Employee> employeePage =
                employeeRepository.findAll(
                        new Pageable() {
                            @Override
                            public int getPageNumber() {
                                return pageNum;
                            }

                            @Override
                            public int getPageSize() {
                                return pageSize;
                            }

                            @Override
                            public long getOffset() {
                                // logger.info("=======>：" + (pageNum - 1) * pageSize);
                                return (pageNum - 1) * pageSize;
                            }

                            @Override
                            public Sort getSort() {
                                return Sort.by("id");
                            }

                            @Override
                            public Pageable next() {
                                return null;
                            }

                            @Override
                            public Pageable previousOrFirst() {
                                return null;
                            }

                            @Override
                            public Pageable first() {
                                return null;
                            }

                            @Override
                            public boolean hasPrevious() {
                                return false;
                            }
                        });
        List<Employee> list = employeePage.getContent();
        long count = employeePage.getTotalElements();
        int totalPages = employeePage.getTotalPages();
        map.put("list", list);
        map.put("count", count);
        map.put("totalPages", totalPages);
        return map;
    }
    /** -------------jpa---------------------- */
}

