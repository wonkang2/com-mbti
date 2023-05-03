package com.commbti.global.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private ExceptionCode exceptionCode;
    public ApiException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
