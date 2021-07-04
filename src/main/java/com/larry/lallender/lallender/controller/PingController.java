package com.larry.lallender.lallender.controller;

import com.larry.lallender.lallender.dto.AuthUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/api/ping")
    public String ping(AuthUser authUser) {
        return "pong";
    }

}
