package com.commbti.global.page;

import com.commbti.domain.admin.dto.*;
import com.commbti.domain.bulletinboard.dto.BoardResponseDto;
import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.comment.dto.CommentResponseDto;
import com.commbti.domain.comment.entity.Comment;
import com.commbti.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponseDto<T> {
    private PageInfo pageInfo;
    private List<T> pageContents;

    private PageResponseDto(PageInfo pageInfo, List<T> pageContents) {
        this.pageInfo = pageInfo;
        this.pageContents = pageContents;
    }

        public static PageResponseDto<BulletinResponseDto> toBulletinPage(Page<BoardResponseDto> bulletinPage) {
        PageInfo pageInfo = new PageInfo(bulletinPage);
        List<BoardResponseDto> boardResponseDtoList = bulletinPage.toList();

        return new PageResponseDto(pageInfo, boardResponseDtoList);
    }

    public static PageResponseDto<AdminBulletinResponseDto> toAdminBulletinPage(Page<Bulletin> bulletinPage) {
        PageInfo pageInfo = new PageInfo(bulletinPage);
        List<AdminBulletinResponseDto> adminBulletinResponseDtoList = bulletinPage.stream().map(bulletin -> bulletin.toAdminResponseDto())
                .collect(Collectors.toList());

        return new PageResponseDto(pageInfo, adminBulletinResponseDtoList);
    }

    public static PageResponseDto<AdminCommentResponseDto> toAdminCommentPage(Page<Comment> commentPage) {
        PageInfo pageInfo = new PageInfo(commentPage);
        List<AdminCommentResponseDto> adminCommentResponseDtoList = commentPage.stream().map(comment -> comment.toAdminResponseDto())
                .collect(Collectors.toList());

        return new PageResponseDto(pageInfo, adminCommentResponseDtoList);
    }

    public static PageResponseDto<CommentResponseDto> toCommentPage(Page<Comment> commentPage) {
        PageInfo pageInfo = new PageInfo(commentPage);
        List<CommentResponseDto> commentResponseDtoList = commentPage.stream().map(comment -> comment.toResponseDto())
                .collect(Collectors.toList());

        return new PageResponseDto(pageInfo, commentResponseDtoList);
    }


    public static PageResponseDto<AdminCommentResponseDto> toAdminMemberPage(Page<Member> memberPage) {
        PageInfo pageInfo = new PageInfo(memberPage);
        List<AdminMemberResponseDto> adminMemberResponseDtoList = memberPage.stream().map(member -> member.toAdminResponseDto())
                .collect(Collectors.toList());

        return new PageResponseDto(pageInfo, adminMemberResponseDtoList);
    }

}
