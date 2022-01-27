package com.microservice.auth.service.impl;

import com.microservice.auth.entity.User;
import com.microservice.auth.repository.IUserRepository;
import com.microservice.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    IUserRepository userRepository;

    @Override
    public Page<User> getList(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        userPage.getContent().stream().forEach(u->{
            u.setPassword("");
            u.setSalt("");
        });
        return userPage;
    }

    @Override
    public User findById(Long uid) {
        return userRepository.findUserByUid(uid);
    }

    @Override
    public void add(User entity) {
        userRepository.save(entity);
    }

    @Override
    public void update(User entity) {
        userRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
