package com.fancyfrog.security.authentication.common.exception.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Getter
public class ErrorResponse {

    private final HttpStatus status;
    private final String message;
    private final ErrorCode errorCode;
    private final LocalDateTime timestamp;

    public ErrorResponse(HttpStatus status, String message, ErrorCode errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(final String message,final ErrorCode errorCode,HttpStatus status){
        return new ErrorResponse(status, message, errorCode);
    }
}
