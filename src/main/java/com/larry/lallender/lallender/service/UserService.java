package com.larry.lallender.lallender.service;

import com.larry.lallender.lallender.domain.entity.User;
import com.larry.lallender.lallender.domain.repository.UserRepository;
import com.larry.lallender.lallender.dto.UserCreateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserService 는 User 도메인의 기본적인 CRUD를 담당한다.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User create(UserCreateReq req) {
        userRepository.findByEmail(req.getEmail())
                      .ifPresent(u -> {
                          throw new RuntimeException("already exists");
                      });
        return userRepository.save(User.builder()
                                       .name(req.getName())
                                       .password(req.getPassword())
                                       .email(req.getEmail())
                                       .birthday(req.getBirthday())
                                       .build());
    }

    @Transactional
    public User findById(Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new RuntimeException("not found"));
    }

    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new RuntimeException("not found"));
    }
}
