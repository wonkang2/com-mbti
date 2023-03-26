package com.commbti.domain.bulletinboard.dto;

import com.commbti.domain.file.entity.ImageFile;
import com.commbti.domain.member.entity.MbtiType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private MbtiType mbtiType;
    private LocalDateTime createdAt;

    private Long viewCount;
    private Long commentCount;
    private String thumbnailPath;

    private BoardResponseDto() {
    }

    /* Repository 에서 바로 변환 */
    @Builder
    public BoardResponseDto(Long id, String title, String content, MbtiType mbtiType, LocalDateTime createdAt, Long viewCount, Long commentCount, String thumbnailPath) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.mbtiType = mbtiType;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.thumbnailPath = thumbnailPath;
    }
}
