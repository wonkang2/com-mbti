package com.commbti.domain.board.dto;

import com.commbti.domain.member.entity.MbtiType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardListResponseDto {
    private Long id;
    private String title;
    private String username;
    private MbtiType mbtiType;
    private LocalDateTime createdAt;

    private BoardListResponseDto() {
    }
    @Builder
    public BoardListResponseDto(Long id, String title, String username, MbtiType mbtiType, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.mbtiType = mbtiType;
        this.createdAt = createdAt;
    }
}
