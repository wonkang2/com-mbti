package com.commbti.domain.admin.service;

import com.commbti.domain.admin.dto.AdminCommentResponseDto;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.repository.MemberRepository;
import com.commbti.global.page.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public PageResponseDto<AdminCommentResponseDto> findMemberPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Sort sort = Sort.by(Sort.Order.asc("id"));
        pageRequest.withSort(sort);
        Page<Member> memberPage = memberRepository.findAll(pageRequest);
        PageResponseDto<AdminCommentResponseDto> response = PageResponseDto.toAdminMemberPage(memberPage);

        return response;
    }


}
