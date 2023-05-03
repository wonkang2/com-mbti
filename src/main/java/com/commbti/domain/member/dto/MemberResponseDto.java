package com.commbti.domain.member.dto;

import com.commbti.domain.member.entity.MbtiType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberResponseDto {
    private Long memberId;
    private String email;
    private String username;
    private MbtiType mbtiType;

    @Builder
    public MemberResponseDto(Long memberId, String email, String username, MbtiType mbtiType) {
        this.memberId = memberId;
        this.email = email;
        this.username = username;
        this.mbtiType = mbtiType;
    }
}
