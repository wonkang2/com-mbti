package com.commbti.domain.bulletinboard.dto;

import com.commbti.domain.member.entity.MbtiType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BulletinResponseDto {
    private String title;
    private String content;
    private String filePath;
    private String nickname;
    private MbtiType mbtiType;
    private LocalDateTime createdAt;

    private BulletinResponseDto() {
    }
    @Builder
    public BulletinResponseDto(String title, String content, String filePath, String nickname, MbtiType mbtiType, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.filePath = filePath;
        this.nickname = nickname;
        this.mbtiType = mbtiType;
        this.createdAt = createdAt;
    }
}
