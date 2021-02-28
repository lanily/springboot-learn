package com.hsiao.security.mapper;

import com.hsiao.security.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 *
 * 数据库表user的mapper类
 */
@Mapper
@Component
public interface UserMapper {
    /**
     * 从数据库中查询用户
     * @param username
     * @return
     */
    @Select("select * from user where username = #{username}")
    User selectByUsername(@Param("username") String username);
}
