package com.commbti.domain.comment.service;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.service.BulletinService;
import com.commbti.domain.comment.dto.CommentRequestDto;
import com.commbti.domain.comment.dto.CommentResponseDto;
import com.commbti.domain.comment.entity.Comment;
import com.commbti.domain.comment.repository.CommentRepository;
import com.commbti.domain.member.entity.Member;
import com.commbti.global.exception.ApiException;
import com.commbti.global.exception.ExceptionCode;
import com.commbti.global.page.PageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final BulletinService bulletinService;

    // 댓글 생성
    @Transactional
    public void createComment(Member member, Long bulletinId, CommentRequestDto commentRequestDto) {

        Bulletin findBulletin = bulletinService.getVerifiedBulletin(bulletinId);
        String content = commentRequestDto.getContent();

        Comment comment = Comment.create(member, findBulletin, content);

        commentRepository.save(comment);
    }

    // 게시글에 해당하는 댓글 조회
    public PageResponseDto<CommentResponseDto> findCommentPageByBulletinId(Long bulletinId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Comment> commentPage = commentRepository.findPageWithMemberMbtiByBulletinId(bulletinId, pageRequest);

        PageResponseDto<CommentResponseDto> response = PageResponseDto.toCommentPage(commentPage);
        return response;
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Member member, Long commentId, CommentRequestDto commentRequestDto) {
        Comment foundComment = getVerifiedComment(commentId);

        verifyAuthority(member, foundComment);

        foundComment.update(commentRequestDto.getContent());

        return foundComment.toResponseDto();
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Member member, Long commentId) {
        Comment foundComment = getVerifiedComment(commentId);

        verifyAuthority(member, foundComment);

        commentRepository.delete(foundComment);
    }

    @Transactional
    public void deleteByBulletinId(Long bulletinId) {
        commentRepository.deleteAllByBulletin_Id(bulletinId);

    }

    protected Comment getVerifiedComment(Long commentId) {
        Comment foundComment = commentRepository.findById(commentId).orElseThrow(
                () -> new ApiException(ExceptionCode.COMMENT_NOT_FOUND)
        );
        return foundComment;
    }

    protected void verifyAuthority(Member member, Comment comment) {
        if (!comment.getMember().getId().equals(member.getId())) {
            log.info("요청한 유저의 memberId와 comment의 memberId가 일치하지 않음. memberId:{}, comment.member.id:{}", member.getId(), comment.getMember().getId());
            throw new ApiException(ExceptionCode.FORBIDDEN);
        }
    }
}
