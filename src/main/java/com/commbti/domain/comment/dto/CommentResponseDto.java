package com.commbti.domain.comment.dto;

import com.commbti.domain.member.entity.MbtiType;
import com.commbti.global.date.DateUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponseDto {
    private Long memberId;
    private Long commentId;
    private MbtiType mbtiType;
    private String content;
    private String createdAt;

    public CommentResponseDto(Long memberId, Long commentId, MbtiType mbtiType, String content, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.commentId = commentId;
        this.mbtiType = mbtiType;
        this.content = content;
        this.createdAt = DateUtils.convertToTimesAgo(createdAt);
    }
}
