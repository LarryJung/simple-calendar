package com.larry.lallender.lallender.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRes {
    private final Long id;
    private final String name;
    private final String email;
    private final LocalDate birthday;
}
