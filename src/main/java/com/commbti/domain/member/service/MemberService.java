package com.commbti.domain.member.service;

import com.commbti.domain.member.dto.MemberPatchDto;
import com.commbti.domain.member.dto.MemberResponseDto;
import com.commbti.domain.member.dto.MemberSignupDto;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.repository.MemberRepository;
import com.commbti.global.exception.BusinessLogicException;
import com.commbti.global.exception.ExceptionCode;
import com.commbti.global.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Transactional
    public Long signup(MemberSignupDto request) {
        Member createMember = Member.createMember(request.getEmail(), request.getUsername(), request.getPassword(), passwordEncoder, request.getMbti());
        memberRepository.save(createMember);
        return createMember.getId();
    }

    @Transactional
    public MemberResponseDto modify(Member member, Long memberId, MemberPatchDto request) {
        if (!member.getId().equals(memberId)) {
            throw new IllegalArgumentException("접근권한이 없습니다.");
        }
        member.modifyInfo(request.getPassword(), passwordEncoder, request.getMbtiType());
        memberRepository.save(member);

        return member.toResponseDto();
    }

    public MemberResponseDto findOneByEmail(String email) {
        Member foundMember = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원입니다.")
        );
        return foundMember.toResponseDto();
    }

    @Transactional
    public void findPassword(String email, String username) {
        // 쿼리 파라미터로 받은 email에 해당하는 Member Entity 조회
        Member foundMember = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원입니다.")
        );
        // 조회한 Member Entity의 username과 쿼리 파라미터로 받은 username이 일치하는가?
        if (!foundMember.getUsername().equals(username)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
        String newPassword = foundMember.resetPassword(passwordEncoder);
        mailService.sendNewPassword(foundMember.getEmail(), newPassword);
    }


    public Member findMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("존재하지 않는 회원입니다. : " + username)
        );
    }

    public void checkDuplicateUsername(String username) {
        boolean doesItExist = memberRepository.existsByUsername(username);
        if (doesItExist == true) {
            throw new BusinessLogicException(ExceptionCode.USERNAME_ALREADY_EXISTS);
        }
    }

    public String verifyEmail(String email) {
        boolean doesItExist = memberRepository.existsByEmail(email);
        if (doesItExist == true) {
            throw new BusinessLogicException(ExceptionCode.EMAIL_ALREADY_EXISTS);
        }
        return mailService.sendAuthNumber(email);
    }

}
