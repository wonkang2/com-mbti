package com.commbti.domain.comment.dto;

import com.commbti.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@Setter
public class CommentRequestDto {
    @NotBlank(message = "댓글은 공백으로만 이루어질 수 없으며, 필수값입니다.")
    @Size(max = 255, message = "댓글은 최대 255자까지 입력하실 수 있습니다.")
    private String content;
}
