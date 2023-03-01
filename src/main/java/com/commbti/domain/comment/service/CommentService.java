package com.commbti.domain.comment.service;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.service.BulletinService;
import com.commbti.domain.comment.dto.CommentRequestDto;
import com.commbti.domain.comment.dto.CommentResponseDto;
import com.commbti.domain.comment.entity.Comment;
import com.commbti.domain.comment.repository.CommentRepository;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final BulletinService bulletinService;

    // 댓글 생성
    public void createComment(Long memberId, Long bulletinId, CommentRequestDto commentRequestDto) {
        Member findMember = memberService.findMember(memberId);
        Bulletin findBulletin = bulletinService.getVerifiedBulletin(bulletinId);
        String content = commentRequestDto.getContent();

        Comment comment = Comment.create(findMember, findBulletin, content);

        commentRepository.save(comment);
    }

    // 댓글 수정
    public Long updateComment(Long memberId, Long commentId, CommentRequestDto commentRequestDto) {
        Member findMember = memberService.findMember(memberId);
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        Comment findComment = optionalComment.orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        String updateContent = commentRequestDto.getContent();

        findComment.update(updateContent);

        return findComment.getId();
    }

    // 댓글 삭제
    public void deleteComment(Long memberId, Long commentId) {
        Member findMember = memberService.findMember(memberId);
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        Comment findComment = optionalComment.orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        commentRepository.delete(findComment);
    }

    // 게시글에 해당하는 댓글 조회
    public List<CommentResponseDto> findAllByBulletinId(Long bulletinId) {
        List<Comment> comments = commentRepository.findAllByBulletinId(bulletinId);

        List<CommentResponseDto> responseDtoList =
                comments.stream().map(c -> c.toResponseDto()).collect(Collectors.toList());

        return responseDtoList;
    }


}
