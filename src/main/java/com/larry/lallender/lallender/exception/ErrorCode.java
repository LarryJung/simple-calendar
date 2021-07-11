package com.larry.lallender.lallender.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    PASSWORD_NOT_MATCH("비밀번호 불일치", HttpStatus.UNAUTHORIZED),
    ALREADY_EXISTS_USER("이미 있는 계정", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("존재하지 않는 계정", HttpStatus.UNAUTHORIZED),
    VALIDATION_FAIL("값이 유효하지 않음", HttpStatus.BAD_REQUEST),
    BAD_REQUEST("잘못된 접근", HttpStatus.BAD_REQUEST),
    ALREADY_DECIDED_ENGAGEMENT("이미 참석 여부가 결정된 이벤트입니다.", HttpStatus.BAD_REQUEST),
    EVENT_CREATE_OVERLAPPED_PERIOD("이벤트 기간 중복", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
