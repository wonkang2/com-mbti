package com.commbti.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private int status;
    private String exception;
    private String message;

    private ErrorResponse(int status, String exception, String message) {
        this.status = status;
        this.exception = exception;
        this.message = message;
    }

    public static ErrorResponse of(ExceptionCode exceptionCode) {
        return new ErrorResponse(exceptionCode.getStatus(), exceptionCode.name(), exceptionCode.getMessage());
    }
}
