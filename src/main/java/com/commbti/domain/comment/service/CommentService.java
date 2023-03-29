package com.commbti.domain.comment.service;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.service.BulletinService;
import com.commbti.domain.comment.dto.CommentPageDto;
import com.commbti.domain.comment.dto.CommentRequestDto;
import com.commbti.domain.comment.dto.CommentResponseDto;
import com.commbti.domain.comment.entity.Comment;
import com.commbti.domain.comment.repository.CommentRepository;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final BulletinService bulletinService;

    // 댓글 생성 TODO
    public void createComment(Long memberId, Long bulletinId, CommentRequestDto commentRequestDto) {
        Member findMember = memberService.findMember(memberId);
        Bulletin findBulletin = bulletinService.getVerifiedBulletin(bulletinId);
        String content = commentRequestDto.getContent();

        Comment comment = Comment.create(findMember, findBulletin, content);

        commentRepository.save(comment);
    }

    // 게시글에 해당하는 댓글 조회
    @Transactional(readOnly = true)
    public CommentPageDto findCommentPageByBulletinId(Long bulletinId, int page, int size) {
        log.trace("findCommentPageByBulletinId() 메서드 호출");
        log.debug("bulletinId: {}, page: {}, size: {}", bulletinId, page, size);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Comment> commentPage = commentRepository.findPageWithMemberMbtiByBulletinId(bulletinId, pageRequest);

        CommentPageDto response = CommentPageDto.toPageDto(commentPage);
        log.trace("findCommentPageByBulletinId() 메서드 종료");
        return response;
    }

    // 댓글 수정 TODO
    public Long updateComment(Long memberId, Long commentId, CommentRequestDto commentRequestDto) {
        Member findMember = memberService.findMember(memberId);
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        Comment findComment = optionalComment.orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        String updateContent = commentRequestDto.getContent();

        findComment.update(updateContent);

        return findComment.getId();
    }

    // 댓글 삭제 TODO
    public void deleteComment(Long memberId, Long commentId) {
        Member findMember = memberService.findMember(memberId);
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        Comment findComment = optionalComment.orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        commentRepository.delete(findComment);
    }

    public void deleteAllByBulletinId(Long bulletinId) {
        commentRepository.deleteAllByBulletin_Id(bulletinId);

    }
}
