package com.commbti.domain.comment.dto;

import com.commbti.domain.member.entity.MbtiType;
import com.commbti.global.date.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private MbtiType mbtiType;
    private String content;
    private String createdAt;

    public CommentResponseDto(Long commentId, MbtiType mbtiType, String content, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.mbtiType = mbtiType;
        this.content = content;
        this.createdAt = DateUtils.convertToTimesAgo(createdAt);
    }
}
