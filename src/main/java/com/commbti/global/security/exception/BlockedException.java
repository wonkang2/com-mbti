package com.commbti.global.security.exception;

import org.springframework.security.core.AuthenticationException;

public class BlockedException extends AuthenticationException {
    public BlockedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BlockedException(String msg) {
        super(msg);
    }
}
