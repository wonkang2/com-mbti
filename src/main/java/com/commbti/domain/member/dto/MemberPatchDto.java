package com.commbti.domain.member.dto;

import com.commbti.domain.member.entity.MbtiType;
import lombok.*;

@NoArgsConstructor
@Getter @Setter
public class MemberPatchDto {
    private String email;
    private String username;
    private String password;
    private MbtiType mbtiType;

    @Builder
    public MemberPatchDto(String email, String username, MbtiType mbtiType) {
        this.email = email;
        this.username = username;
        this.mbtiType = mbtiType;
    }
}
