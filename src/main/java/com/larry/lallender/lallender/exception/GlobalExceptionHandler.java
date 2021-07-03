package com.larry.lallender.lallender.exception;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CalendarException.class)
    public ResponseEntity<ErrorResponse> handle(CalendarException ex) {
        final ErrorCode errorCode = ex.getErrorCode();
        return new ResponseEntity(new ErrorResponse(errorCode, errorCode.getMessage()),
                                  errorCode.getHttpStatus());
    }

    @Data
    public static class ErrorResponse {
        private final ErrorCode errorCode;
        private final String errorMessage;
    }
}
