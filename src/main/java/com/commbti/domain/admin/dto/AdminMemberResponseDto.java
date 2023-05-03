package com.commbti.domain.admin.dto;

import com.commbti.domain.member.entity.MbtiType;
import com.commbti.domain.member.entity.MemberRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminMemberResponseDto {
    private Long id;
    private String email;
    private String username;
    private MbtiType mbtiType;
    private MemberRole role;
    private LocalDateTime lastLoginDate;
    private boolean isAccountNonLocked;
    private boolean isNonBlocked;


    @Builder
    public AdminMemberResponseDto(Long id, String email, String username, MbtiType mbtiType, MemberRole role, LocalDateTime lastLoginDate, boolean isAccountNonLocked, boolean isNonBlocked) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.mbtiType = mbtiType;
        this.role = role;
        this.lastLoginDate = lastLoginDate;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isNonBlocked = isNonBlocked;
    }
}
