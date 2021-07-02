package com.larry.lallender.lallender;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class User {
    private Long id;
    private String name;
    // TODO need validation
    private String email;
    private String password;
    private LocalDate birthDay;

    public User(final Long id,
                final String name,
                final String email,
                final String password,
                final LocalDate birthDay) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDay = birthDay;
    }
}
