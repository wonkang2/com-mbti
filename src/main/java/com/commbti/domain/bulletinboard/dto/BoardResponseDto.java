package com.commbti.domain.bulletinboard.dto;

import com.commbti.domain.member.entity.MbtiType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String username;
    private MbtiType mbtiType;
    private LocalDateTime createdAt;

    private BoardResponseDto() {
    }
    @Builder
    public BoardResponseDto(Long id, String title, String username, MbtiType mbtiType, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.mbtiType = mbtiType;
        this.createdAt = createdAt;
    }
}
