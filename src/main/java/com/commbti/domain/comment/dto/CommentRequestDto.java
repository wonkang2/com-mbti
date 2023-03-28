package com.commbti.domain.comment.dto;

import com.commbti.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class CommentRequestDto {
    private String content;
}
