package com.commbti.domain.comment.dto;

import com.commbti.domain.member.entity.MbtiType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private String nickname;
    private MbtiType mbtiType;
    private String content;
    private LocalDateTime createdAt;

}
