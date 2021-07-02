package com.larry.lallender.lallender.service;

import com.larry.lallender.lallender.domain.entity.User;
import com.larry.lallender.lallender.dto.UserCreateReq;
import com.larry.lallender.lallender.dto.UserSignUpReq;
import com.larry.lallender.lallender.util.BCryptEncryptor;
import com.larry.lallender.lallender.util.Encryptor;

import javax.servlet.http.HttpSession;

public class LoginService {
    private final String LOGIN_SESSION_KEY = "USER_ID";
    private final UserService userService;
    private final Encryptor encryptor = new BCryptEncryptor();

    public LoginService(UserService userService) {
        this.userService = userService;
    }

    public void signUp(UserSignUpReq req, HttpSession session) {
        User newUser = userService.create(new UserCreateReq(req.getName(),
                                                            req.getEmail(),
                                                            encryptor.encrypt(req.getPassword()),
                                                            req.getBirthDay()));
        session.setAttribute(LOGIN_SESSION_KEY,
                             newUser.getId());
    }

    public void signIn(UserSignUpReq req, HttpSession session) {
        Long userId = (Long) session.getAttribute(LOGIN_SESSION_KEY);
        if (userId != null) {
            return;
        }
        User user = userService.findByEmail(req.getEmail());
        if (encryptor.isMatch(req.getPassword(),
                              user.getPassword())) {
            session.setAttribute(LOGIN_SESSION_KEY,
                                 user.getId());
        } else {
            throw new RuntimeException("password not match");
        }
    }

}
