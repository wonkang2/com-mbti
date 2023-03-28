package com.commbti.domain.comment.dto;

import com.commbti.domain.bulletinboard.dto.BoardResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.comment.entity.Comment;
import com.commbti.domain.page.dto.PageInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CommentPageDto {
    private PageInfo pageInfo;
    private List<CommentResponseDto> commentResponseDtoList;

    private CommentPageDto(PageInfo pageInfo, List<CommentResponseDto> commentResponseDtoList) {
        this.pageInfo = pageInfo;
        this.commentResponseDtoList = commentResponseDtoList;
    }

    public static CommentPageDto toPageDto(Page<Comment> commentPage) {
        PageInfo pageInfo = new PageInfo(commentPage);
        List<CommentResponseDto> commentResponseDtoList = commentPage.stream().map(comment -> comment.toResponseDto())
                .collect(Collectors.toList());

        return new CommentPageDto(pageInfo, commentResponseDtoList);
    }
}
