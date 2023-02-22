package com.commbti.domain.board.dto;

import com.commbti.domain.member.entity.MbtiType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private String title;
    private String content;
    private String filePath;
    private String nickname;
    private MbtiType mbtiType;
    private LocalDateTime createdAt;

    private BoardResponseDto() {
    }
    @Builder
    public BoardResponseDto(String title, String content, String filePath, String nickname, MbtiType mbtiType, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.filePath = filePath;
        this.nickname = nickname;
        this.mbtiType = mbtiType;
        this.createdAt = createdAt;
    }
}
