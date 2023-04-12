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

    public PageResponseDto<AdminCommentResponseDto> findCommentPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Sort sort = Sort.by(Sort.Order.asc("id"));
        pageRequest.withSort(sort);
        Page<Comment> commentPage = commentRepository.findAll(pageRequest);

        PageResponseDto<AdminCommentResponseDto> response = PageResponseDto.toAdminCommentPage(commentPage);
        return response;
    }

    public void deleteOne(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        commentRepository.delete(comment);
    }


}
