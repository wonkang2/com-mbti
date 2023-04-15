package com.commbti.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity handleBusinessLogicException(BusinessLogicException businessLogicException) {
        ErrorResponse response = ErrorResponse.of(businessLogicException.getExceptionCode());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
