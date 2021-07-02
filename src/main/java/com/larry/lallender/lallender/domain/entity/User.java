package com.larry.lallender.lallender.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class User {
    private Long id;
    private String name;
    // TODO need validation, unique
    private String email;
    private String password;
    private LocalDate birthDay;
}
