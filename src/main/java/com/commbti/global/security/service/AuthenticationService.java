package com.commbti.global.security.service;

import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.repository.MemberRepository;
import com.commbti.global.security.exception.BlockedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final MemberRepository memberRepository;


    public void increaseLoginFailCount(String username) {
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        if (optionalMember.isEmpty()) {
            return;
        }
        optionalMember.get().increaseLoginFailCount();
    }

    public void initLogin(String username) {
        Member foundMember = memberRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("존재하지 않는 회원입니다. : " + username));
        if (!foundMember.isNonBlocked()) {
            log.info("차단된 계정의 로그인 시도: {}" , username);
            throw new BlockedException("차단된 계정입니다. : " + username);
        }
        foundMember.updateLoginInfo();
    }
}
