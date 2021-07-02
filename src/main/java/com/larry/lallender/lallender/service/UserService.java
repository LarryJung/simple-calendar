package com.larry.lallender.lallender.service;

import com.larry.lallender.lallender.domain.entity.User;
import com.larry.lallender.lallender.domain.repository.UserRepository;
import com.larry.lallender.lallender.dto.UserCreateReq;

/**
 * UserService 는 User 도메인의 기본적인 CRUD를 담당한다.
 */
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(UserCreateReq req) {
        userRepository.findByEmail(req.getEmail())
                      .ifPresent(u -> {
                          throw new RuntimeException("already exists");
                      });
        return userRepository.save(User.builder()
                                       .name(req.getName())
                                       .password(req.getPassword())
                                       .email(req.getEmail())
                                       .birthDay(req.getBirthDay())
                                       .build());
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new RuntimeException("not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new RuntimeException("not found"));
    }
}
