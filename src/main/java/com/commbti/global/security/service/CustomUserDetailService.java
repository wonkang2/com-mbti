package com.commbti.global.security.service;

import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.service.MemberService;
import com.commbti.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomUserDetailService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member foundMember = memberService.findMember(username);
        CustomUserDetails customUserDetails = new CustomUserDetails(foundMember);

        return customUserDetails;
    }
}
