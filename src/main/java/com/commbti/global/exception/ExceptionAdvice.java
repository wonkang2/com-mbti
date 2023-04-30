package com.commbti.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    /**
     * API Exception
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleApiException(ApiException apiException) {
        ErrorResponse response = ErrorResponse.of(apiException.getExceptionCode());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors()
                .get(0)
                .getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    /**
     * View Exception
     */
    @ExceptionHandler(ViewException.class)
    public String handleViewException(Model model,
                                      ViewException viewException) {
        String imagePath = "/images/error/";
        ExceptionCode exceptionCode = viewException.getExceptionCode();
        model.addAttribute("image", imagePath + exceptionCode.getStatus() + ".jpeg");
        model.addAttribute("message", exceptionCode.getMessage());
        return "error/error";
    }
}
