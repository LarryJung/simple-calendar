package com.larry.lallender.lallender.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    PASSWORD_NOT_MATCH("비밀번호 불일치", HttpStatus.UNAUTHORIZED),
    ALREADY_EXISTS_USER("이미 있는 계정", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("존재하지 않는 계정", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
