package com.commbti.domain.member.service;

import com.commbti.domain.member.dto.MemberPatchDto;
import com.commbti.domain.member.dto.MemberResponseDto;
import com.commbti.domain.member.dto.MemberSignupDto;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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


    public Member findMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("존재하지 않는 회원입니다. : " + username)
        );
    }

}
