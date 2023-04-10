package com.commbti.global.security.handler;

import com.commbti.global.security.exception.BlockedException;
import com.commbti.global.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
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
@RequiredArgsConstructor
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final AuthenticationService authenticationService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        log.info("=== Exception: {} , username: {} ===",exception, request.getParameter("username"));
        if (exception instanceof BadCredentialsException) {
            String username = request.getParameter("username");
            authenticationService.increaseLoginFailCount(username);
            errorMessage = "아이디 또는 비밀번호를 확인해주세요.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "아이디 또는 비밀번호를 확인해주세요.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "잠시 후 다시 요청해주세요.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "인증이 거부되었습니다.";
        } else if (exception instanceof LockedException) {
            errorMessage = "5회이상 비밀번호가 틀려 잠긴 계정입니다.";
        }  else if (exception instanceof BlockedException) {
            errorMessage = "관리자에 의해 차단된 계정입니다.";
        } else {
            errorMessage = "알 수 없는 에러입니다. 관리자에게 문의하세요.";
        }
        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
        setDefaultFailureUrl("/login?error=" + errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
