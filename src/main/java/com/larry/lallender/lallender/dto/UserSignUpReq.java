package com.larry.lallender.lallender.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
public class UserSignUpReq {
    private final String name;
    @Email(message = "유효하지 않은 이메일입니다.")
    private final String email;
    @Min(value = 6, message = "6자리 이상 입력해주세요.")
    private final String password;
    private final LocalDate birthday;
}
