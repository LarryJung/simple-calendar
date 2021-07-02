package com.larry.lallender.lallender.dto;

import lombok.Data;

@Data
public class UserSignInReq {
    private final String email;
    private final String password;
}
