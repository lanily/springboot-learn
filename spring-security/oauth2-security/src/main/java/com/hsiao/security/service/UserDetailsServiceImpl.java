package com.hsiao.security.service;

import com.hsiao.security.bean.User;
import com.hsiao.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 
 * UserDetailsService的实现类，用于在程序中引入一个自定义的AuthenticationProvider，实现数据库访问模式的验证
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    UserMapper mapper;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = mapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("数据库中无此用户！");
        }
        return user;
    }
}
