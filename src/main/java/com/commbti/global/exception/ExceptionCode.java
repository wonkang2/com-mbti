package com.commbti.global.exception;

import lombok.Getter;

public enum ExceptionCode {
    USERNAME_ALREADY_EXISTS(409, "해당 아이디는 이미 존재합니다."),
    EMAIL_ALREADY_EXISTS(409, "해당 이메일은 이미 존재합니다.");
    @Getter
    private int status;
    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
