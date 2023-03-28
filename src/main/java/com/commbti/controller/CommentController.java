package com.commbti.controller;

import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.service.BulletinService;
import com.commbti.domain.comment.dto.CommentPageDto;
import com.commbti.domain.comment.dto.CommentRequestDto;
import com.commbti.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/bulletin-board/bulletins/{bulletin-id}")
@Controller
public class CommentController {

    private final CommentService commentService;
    private final BulletinService bulletinService;

    @PostMapping("/comments/save")
    public String postComment(@PathVariable("bulletin-id") Long bulletinId,
                              CommentRequestDto request) {
        Long dummyMemberId = 1L;
        commentService.createComment(dummyMemberId, bulletinId, request);
        return "redirect:/bulletin-board/bulletins/" + bulletinId;
    }

    // TODO: 댓글 조회 부분을 AJAX 통신을 이용하여 로직 구현시 삭제 예정
    @GetMapping("/comments")
    public String getBulletin(@PathVariable("bulletin-id") Long bulletinId,
                              @RequestParam int page,
                              @RequestParam int size,
                              Model model) {
        log.trace("getBulletin() 메서드 호출");
        BulletinResponseDto bulletinResponse = bulletinService.findOne(bulletinId);
        CommentPageDto commentResponse = commentService.findCommentPageByBulletinId(bulletinId, page, size);
        CommentRequestDto commentRequestDto = new CommentRequestDto();

        model.addAttribute("bulletin", bulletinResponse);
        model.addAttribute("commentPage", commentResponse);
        model.addAttribute(commentRequestDto);
        log.trace("getBulletin() 메서드 종료");
        return "/bulletin-board/bulletin";
    }
}
