package com.commbti.global.security.handler;

import com.commbti.domain.member.entity.Member;
import com.commbti.global.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    private final AuthenticationService authenticationService;
    private RequestCache requestCache = new HttpSessionRequestCache(); // 로그인페이지 이동 전 요청 저장되어있음
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy(); // 로그인 진행 후 리다이렉트전략

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Member member = (Member) authentication.getPrincipal();
        authenticationService.initLogin(member.getUsername());

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if(savedRequest != null){
            redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
        }else{
            redirectStrategy.sendRedirect(request, response, "/");
        }
    }


}
