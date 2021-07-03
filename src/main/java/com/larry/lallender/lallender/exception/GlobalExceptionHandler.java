package com.larry.lallender.lallender.exception;

import lombok.Data;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CalendarException.class)
    public ResponseEntity<ErrorResponse> handle(CalendarException ex) {
        final ErrorCode errorCode = ex.getErrorCode();
        return new ResponseEntity(new ErrorResponse(errorCode, errorCode.getMessage()),
                                  errorCode.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException ex) {
        return new ResponseEntity(new ErrorResponse(ErrorCode.VALIDATION_FAIL,
                                                    Optional.ofNullable(ex.getBindingResult()
                                                                          .getFieldError())
                                                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                                            .orElse(ErrorCode.VALIDATION_FAIL.getMessage())),
                                  ErrorCode.VALIDATION_FAIL.getHttpStatus());
    }

    @Data
    public static class ErrorResponse {
        private final ErrorCode errorCode;
        private final String errorMessage;
    }
}
