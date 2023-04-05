package com.commbti.controller.api;

import com.commbti.domain.comment.dto.CommentRequestDto;
import com.commbti.domain.comment.dto.CommentResponseDto;
import com.commbti.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity patch(@PathVariable Long commentId,
                                @RequestBody CommentRequestDto commentRequestDto) {
        Long dummyMemberId = 1L;
        CommentResponseDto response = commentService.updateComment(dummyMemberId, commentId, commentRequestDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity delete(@PathVariable Long commentId) {
        Long dummyMemberId = 1L;
        commentService.deleteComment(dummyMemberId, commentId);

        return ResponseEntity.ok().build();
    }

}
