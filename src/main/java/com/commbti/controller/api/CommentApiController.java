package com.commbti.controller.api;

import com.commbti.domain.comment.dto.CommentRequestDto;
import com.commbti.domain.comment.dto.CommentResponseDto;
import com.commbti.domain.comment.service.CommentService;
import com.commbti.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/bulletins/{bulletinId}/comments")
    public ResponseEntity post(@AuthenticationPrincipal Member loginMember,
                              @Min (value = 1, message = "정상적인 요청이 아닙니다.")@PathVariable Long bulletinId,
                              @Valid @RequestBody CommentRequestDto request) {
        commentService.createComment(loginMember, bulletinId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity patch(@AuthenticationPrincipal Member loginMember,
                                @Min (value = 1, message = "정상적인 요청이 아닙니다.")@PathVariable Long commentId,
                                @Valid @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto response = commentService.updateComment(loginMember, commentId, commentRequestDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity delete(@AuthenticationPrincipal Member loginMember,
                                 @Min (value = 1, message = "정상적인 요청이 아닙니다.")@PathVariable Long commentId) {
        commentService.deleteComment(loginMember, commentId);

        return ResponseEntity.ok().build();
    }

}
