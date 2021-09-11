package com.hsiao.springboot.multiquery.repository;

import com.hsiao.springboot.multiquery.entity.Address;
import org.springframework.data.repository.CrudRepository;

/**
 * 地址
 * @author hsiao
 */
public interface AddressRepository extends CrudRepository<Address, Long> {
}
