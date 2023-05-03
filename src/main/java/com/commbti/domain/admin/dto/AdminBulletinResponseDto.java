package com.commbti.domain.admin.dto;

import com.commbti.domain.member.entity.MbtiType;
import com.commbti.domain.member.entity.MemberRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminBulletinResponseDto {
    private Long id;
    private Long memberId;
    private String username;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private Long viewCount;
    private Long commentCount;


    @Builder
    public AdminBulletinResponseDto(Long id, Long memberId, String username, String title, String content, LocalDateTime createdAt, LocalDateTime lastModifiedAt, Long viewCount, Long commentCount) {
        this.id = id;
        this.memberId = memberId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
    }
}
