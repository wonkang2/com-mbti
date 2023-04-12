package com.commbti.controller;

import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.service.BoardService;
import com.commbti.domain.bulletinboard.service.BulletinService;
import com.commbti.domain.comment.dto.CommentResponseDto;
import com.commbti.domain.comment.service.CommentService;
import com.commbti.domain.member.dto.MemberSignupDto;
import com.commbti.domain.member.service.MemberService;
import com.commbti.global.page.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class ViewController {

    private static final int BOARD_SIZE = 10;
    private final MemberService memberService;
    private final BoardService boardService;
    private final BulletinService bulletinService;
    private final CommentService commentService;

    @GetMapping("/")
    public String getRootPage() {
        return "forward:/bulletin-board?page=1&size=10";
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(value = "error", required = false) String error,
                               Model model) {
        model.addAttribute("error", error);
        return "/auth/login";
    }
    @GetMapping("/signup")
    public String getSignupPage(Model model) {
        MemberSignupDto memberSignupDto = new MemberSignupDto();
        model.addAttribute("dto", memberSignupDto);
        return "/auth/signup";
    }

//    @GetMapping("/members/{memberId}/edit")
//    public String getMemberEditPage(@AuthenticationPrincipal Member,
//                                    @PathVariable Long memberId,
//                                    Model model) {
//
//    }

    @GetMapping("/bulletin-board")
    public String getBoardPage(@RequestParam("page") int page,
                               @RequestParam("size") int size,
                               Model model) {
        PageResponseDto<BulletinResponseDto> bulletinPage = boardService.findBulletinPage(page, BOARD_SIZE);
        model.addAttribute("bulletinPage", bulletinPage);
        return "/bulletin-board/board";
    }

    @GetMapping("/bulletin-board/bulletins/post")
    public String getBulletinPostPage() {
        return "/bulletin-board/post";
    }

    @GetMapping("/bulletin-board/bulletins/{bulletin-id}")
    public String getBulletin(@PathVariable("bulletin-id") Long bulletinId,
                              Model model) {
        BulletinResponseDto bulletinResponse = bulletinService.findOne(bulletinId);
        PageResponseDto<CommentResponseDto> commentResponse = commentService.findCommentPageByBulletinId(bulletinId, 1, 10);

        model.addAttribute("bulletin", bulletinResponse);
        model.addAttribute("commentPage", commentResponse);
        return "/bulletin-board/bulletin";
    }

    @GetMapping("/bulletin-board/bulletins/{bulletin-id}/edit")
    public String getEditPage(@PathVariable("bulletin-id") Long bulletinId,
                              Model model) {
        BulletinResponseDto bulletin = bulletinService.findOne(bulletinId);
        model.addAttribute("bulletin", bulletin);
        return "/bulletin-board/edit";
    }

    @GetMapping("/bulletin-board/bulletins/{bulletin-id}/comments")
    public String getBulletin(@PathVariable("bulletin-id") Long bulletinId,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        BulletinResponseDto bulletinResponse = bulletinService.findOne(bulletinId);
        PageResponseDto<CommentResponseDto> commentResponse = commentService.findCommentPageByBulletinId(bulletinId, page, size);

        model.addAttribute("bulletin", bulletinResponse);
        model.addAttribute("commentPage", commentResponse);
        return "/bulletin-board/bulletin";
    }

}
