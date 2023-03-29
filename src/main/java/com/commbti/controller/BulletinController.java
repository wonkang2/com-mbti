package com.commbti.controller;

import com.commbti.domain.bulletinboard.dto.BulletinPostDto;
import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.service.BulletinService;
import com.commbti.domain.comment.dto.CommentPageDto;
import com.commbti.domain.comment.dto.CommentRequestDto;
import com.commbti.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        log.trace("\"/post/save\": postBulletin() 호출");
        log.debug("request: title={}, content={}, files.count={}", request.getTitle(), request.getContent(), request.getFiles().stream().count());
        log.debug("fileName: {}, fileContentType: {}", request.getFiles().get(0).getOriginalFilename(), request.getFiles().get(0).getContentType());
        Long dummyMemberId = 1L;

        Long bulletinId = bulletinService.createBulletin(dummyMemberId, request);
        log.trace("\"/post/save\": postBulletin() 정상");
        return "redirect:/bulletin-board/bulletins/" + bulletinId;
    }

    @GetMapping("/{bulletin-id}")
    public String getBulletin(@PathVariable("bulletin-id") Long bulletinId,
                              Model model) {
        log.trace("getBulletin() 메서드 호출");
        BulletinResponseDto bulletinResponse = bulletinService.findOne(bulletinId);
        CommentPageDto commentResponse = commentService.findCommentPageByBulletinId(bulletinId, 1, 10);
        CommentRequestDto commentRequestDto = new CommentRequestDto();

        model.addAttribute("bulletin", bulletinResponse);
        model.addAttribute("commentPage", commentResponse);
        model.addAttribute(commentRequestDto);

        log.trace("getBulletin() 메서드 종료");
        return "/bulletin-board/bulletin";
    }

    @GetMapping("/{bulletin-id}/edit")
    public String getEditPage(@PathVariable("bulletin-id") Long bulletinId,
                              Model model) {
        log.trace("getEditPage() 호출");
        BulletinResponseDto oldBulletin = bulletinService.findOne(bulletinId);
        BulletinPostDto newBulletin = new BulletinPostDto();
        log.debug("findBulletin: 제목 = {}, 내용 = {}",oldBulletin.getTitle(), oldBulletin.getContent());
        model.addAttribute("oldBulletin", oldBulletin);
        model.addAttribute("newBulletin", newBulletin);
        log.trace("getEditPage() 종료");
        return "/bulletin-board/edit";
    }

    @PostMapping("/{bulletin-id}/save")
    public String patch(@PathVariable("bulletin-id") Long bulletinId,
                        BulletinPostDto bulletinPostDto) {
        log.trace("patch() 호출");
        BulletinResponseDto oldBulletin = bulletinService.findOne(bulletinId);
        BulletinPostDto newBulletin = new BulletinPostDto();
        log.debug("findBulletin: 제목 = {}, 내용 = {}",oldBulletin.getTitle(), oldBulletin.getContent());
        log.trace("patch() 종료");
        return "redirect:/bulletin-board/bulletins/" + bulletinId;
    }

    @PostMapping("/{bulletin-id}/delete")
    public String deleteBulletin(@PathVariable("bulletin-id") Long bulletinId) {
        log.trace("deleteBulletin() 메서드 호출");
        Long dummyMemberId = 1L;
        commentService.deleteAllByBulletinId(bulletinId);
        bulletinService.deleteOne(dummyMemberId, bulletinId);
        log.trace("deleteBulletin() 메서드 종료");

        return "redirect:/";
    }
}
