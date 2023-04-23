package com.commbti.domain.admin.service;

import com.commbti.domain.admin.dto.AdminCommentResponseDto;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.repository.MemberRepository;
import com.commbti.global.page.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AdminMemberService {
    private final MemberRepository memberRepository;

    public void updateBlockStatus(Long memberId) {
        Member foundMember = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        foundMember.updateBlockStatus();
    }

    public PageResponseDto<AdminCommentResponseDto> findMemberPage(String type, String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Sort sort = Sort.by(Sort.Order.asc("id"));
        pageRequest.withSort(sort);
        Page<Member> memberPage;

        if (type.equals("none") && type.equals("none")) {
            memberPage = memberRepository.findAll(pageRequest);
        } else if (type.equals("email")) {
            memberPage = memberRepository.findByEmailStartsWith(pageRequest, keyword);
        } else if (type.equals("username")) {
            memberPage = memberRepository.findByUsernameStartsWith(pageRequest, keyword);
        } else if (type.equals("id")) {
            memberPage = memberRepository.findById(pageRequest, Long.parseLong(keyword));
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        PageResponseDto<AdminCommentResponseDto> response = PageResponseDto.toAdminMemberPage(memberPage);

        return response;
    }

}
