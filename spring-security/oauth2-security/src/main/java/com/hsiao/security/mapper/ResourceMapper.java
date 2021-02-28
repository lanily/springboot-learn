package com.hsiao.security.mapper;

import com.hsiao.security.bean.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wxb on 2018/10/27 0027.
 */
@Component
@Mapper
public interface ResourceMapper {
    /**
     * 从数据库中查询所有资源
     * @return
     */
    @Select("select * from resource ")
    List<Resource> selectAllResource();
}
