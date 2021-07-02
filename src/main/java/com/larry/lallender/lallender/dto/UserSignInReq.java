package com.larry.lallender.lallender.dto;

import lombok.Data;

@Data
public class UserSignInReq {
    private String email;
    private String password;
}
