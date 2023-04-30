package com.commbti.global.exception;

import lombok.Getter;

@Getter
public class ViewException extends RuntimeException {
    private ExceptionCode exceptionCode;
    public ViewException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
