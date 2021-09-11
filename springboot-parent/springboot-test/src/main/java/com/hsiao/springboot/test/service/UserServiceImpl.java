package com.hsiao.springboot.test.service;


import com.hsiao.springboot.test.entity.User;
import com.hsiao.springboot.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: UserServiceImpl
 * @description: TODO
 * @author xiao
 * @create 2021/4/11
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findOne(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public boolean updateUsername(Long id, String username) {
        assert username != null;
        User user = findOne(id);
        if (user == null) {
            return false;
        }
        user.setUsername(username);
        return username.equals(userRepository.save(user).getUsername());
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
