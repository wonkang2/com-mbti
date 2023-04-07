package com.commbti.global.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@Component
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        log.info("=== Exception: {} , username: {} ===",exception, request.getParameter("username"));
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            errorMessage = "아이디 또는 비밀번호를 확인해주세요.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "잠시 후 다시 요청해주세요.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "인증이 거부되었습니다.";
        } else {
            errorMessage = "Error!!!!";
        }
        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
        setDefaultFailureUrl("/login?error=" + errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
