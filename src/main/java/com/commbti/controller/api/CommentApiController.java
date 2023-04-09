package com.commbti.controller.api;

import com.commbti.domain.comment.dto.CommentRequestDto;
import com.commbti.domain.comment.dto.CommentResponseDto;
import com.commbti.domain.comment.service.CommentService;
import com.commbti.domain.member.entity.Member;
import com.commbti.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/bulletins/{bulletinId}/comments")
    public ResponseEntity post(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                              @PathVariable Long bulletinId,
                              @RequestBody CommentRequestDto request) {
        Member loginMember = customUserDetails.getMember();
        log.info("========== {}", request.getContent());
        commentService.createComment(loginMember, bulletinId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity patch(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                @PathVariable Long commentId,
                                @RequestBody CommentRequestDto commentRequestDto) {
        Member loginMember = customUserDetails.getMember();
        CommentResponseDto response = commentService.updateComment(loginMember, commentId, commentRequestDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity delete(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                 @PathVariable Long commentId) {
        Member loginMember = customUserDetails.getMember();
        commentService.deleteComment(loginMember, commentId);

        return ResponseEntity.ok().build();
    }

}
