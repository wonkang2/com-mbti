package com.commbti.domain.admin.dto;

import com.commbti.domain.member.entity.MbtiType;
import com.commbti.domain.member.entity.MemberRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminCommentResponseDto {
    private Long id;
    private Long memberId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;


    @Builder
    public AdminCommentResponseDto(Long id, Long memberId, String username, String content, LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.id = id;
        this.memberId = memberId;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }
}
