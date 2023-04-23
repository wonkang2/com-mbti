package com.commbti.domain.admin.service;

import com.commbti.domain.admin.dto.AdminCommentResponseDto;
import com.commbti.domain.comment.entity.Comment;
import com.commbti.domain.comment.repository.CommentRepository;
import com.commbti.global.page.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AdminCommentService {
    private final CommentRepository commentRepository;

    public PageResponseDto<AdminCommentResponseDto> findCommentPage(String type, String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Sort sort = Sort.by(Sort.Order.asc("id"));
        pageRequest.withSort(sort);
        Page<Comment> commentPage;

        if (type.equals("none") && type.equals("none")) {
            commentPage = commentRepository.findAll(pageRequest);
        } else if (type.equals("memberId")) {
            commentPage = commentRepository.findByMember_id(pageRequest, Long.parseLong(keyword));
        } else if (type.equals("id")) {
            commentPage = commentRepository.findById(pageRequest, Long.parseLong(keyword));
        } else if (type.equals("username")) {
            commentPage = commentRepository.findByMember_username(pageRequest, keyword);
        } else if (type.equals("content")) {
            commentPage = commentRepository.findByContentContains(pageRequest, keyword);
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        PageResponseDto<AdminCommentResponseDto> response = PageResponseDto.toAdminCommentPage(commentPage);
        return response;
    }

    public void deleteOne(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        commentRepository.delete(comment);
    }


}
