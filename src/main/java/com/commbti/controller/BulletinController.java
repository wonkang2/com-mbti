package com.commbti.controller;

import com.commbti.domain.bulletinboard.dto.BulletinPatchDto;
import com.commbti.domain.bulletinboard.dto.BulletinPostDto;
import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.service.BulletinService;
import com.commbti.domain.comment.dto.CommentPageDto;
import com.commbti.domain.comment.dto.CommentRequestDto;
import com.commbti.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/bulletin-board/bulletins")
@Controller
public class BulletinController {

    private final BulletinService bulletinService;
    private final CommentService commentService;

    @GetMapping("/post")
    public String getBulletinPostPage(Model model) {
        BulletinPostDto bulletinPostDto = new BulletinPostDto();
        model.addAttribute(bulletinPostDto);
        return "/bulletin-board/post";
    }

    @PostMapping("/save")
    public String postBulletin(BulletinPostDto request) throws IOException {
        Long dummyMemberId = 1L;

        Long bulletinId = bulletinService.createBulletin(dummyMemberId, request);
        return "redirect:/bulletin-board/bulletins/" + bulletinId;
    }

    @GetMapping("/{bulletin-id}")
    public String getBulletin(@PathVariable("bulletin-id") Long bulletinId,
                              Model model) {
        BulletinResponseDto bulletinResponse = bulletinService.findOne(bulletinId);
        CommentPageDto commentResponse = commentService.findCommentPageByBulletinId(bulletinId, 1, 10);
        CommentRequestDto commentRequestDto = new CommentRequestDto();

        model.addAttribute("bulletin", bulletinResponse);
        model.addAttribute("commentPage", commentResponse);
        model.addAttribute(commentRequestDto);
        return "/bulletin-board/bulletin";
    }

    @GetMapping("/{bulletin-id}/edit")
    public String getEditPage(@PathVariable("bulletin-id") Long bulletinId,
                              Model model) {
        BulletinResponseDto oldBulletin = bulletinService.findOne(bulletinId);
        BulletinPostDto newBulletin = new BulletinPostDto();
        model.addAttribute("oldBulletin", oldBulletin);
        model.addAttribute("newBulletin", newBulletin);
        return "/bulletin-board/edit";
    }

    @PostMapping("/{bulletin-id}/save")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody()
    public ResponseEntity patch(@PathVariable("bulletin-id") Long bulletinId,
                        BulletinPatchDto bulletinPatchDto) {
        Long dummyMemberId = 1L;
        bulletinService.updateBulletin(dummyMemberId, bulletinId, bulletinPatchDto);
        return ResponseEntity.status(HttpStatus.MOVED_TEMPORARILY).header("Location", "http://localhost:8080/bulletin-board/bulletins/" + bulletinId).build();
    }

    @PostMapping("/{bulletin-id}/delete")
    public String deleteBulletin(@PathVariable("bulletin-id") Long bulletinId) {
        Long dummyMemberId = 1L;
        commentService.deleteAllByBulletinId(bulletinId);
        bulletinService.deleteOne(dummyMemberId, bulletinId);

        return "redirect:/";
    }
}
