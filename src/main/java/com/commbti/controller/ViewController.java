package com.commbti.controller;

import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.service.BoardService;
import com.commbti.domain.bulletinboard.service.BulletinService;
import com.commbti.domain.comment.dto.CommentResponseDto;
import com.commbti.domain.comment.service.CommentService;
import com.commbti.domain.member.dto.MemberResponseDto;
import com.commbti.domain.member.dto.MemberSignupDto;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.service.MemberService;
import com.commbti.global.page.PageResponseDto;
import com.commbti.global.validation.ViewCookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ViewController {
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

    @GetMapping("/members/{memberId}/edit")
    public String getMemberEditPage(@AuthenticationPrincipal Member member,
                                    @PathVariable Long memberId,
                                    Model model) {
        if (!member.getId().equals(memberId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
        MemberResponseDto response = member.toResponseDto();
        model.addAttribute("member", response);

        return "/auth/edit";
    }

    @GetMapping("/bulletin-board")
    public String getBoardPage(@RequestParam(value = "type", defaultValue = "none") String type,
                               @RequestParam(value = "keyword", defaultValue = "none") String keyword,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               Model model) {
        PageResponseDto<BulletinResponseDto> bulletinPage = boardService.findBulletinPage(type, keyword, page, size);
        model.addAttribute("bulletinPage", bulletinPage);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "/bulletin-board/board";
    }

    @GetMapping("/bulletin-board/bulletins/post")
    public String getBulletinPostPage() {
        return "/bulletin-board/post";
    }

    @GetMapping("/bulletin-board/bulletins/{bulletin-id}")
    public String getBulletin(@PathVariable("bulletin-id") Long bulletinId,
                              @CookieValue(value = "VIEWNUMBER", required = false) Cookie viewNumberCookie,
                              HttpServletResponse response,
                              Model model) {
        /*
        쿠키가 비어있을 경우? 카운트 증가와 쿠키 추가 -> 쿠키는 24시간동안
        쿠키가 있을 경우? 본 게시물로 아무것도 x
         */
        ViewCookie viewCookie = ViewCookie.validateViewNumberCookie(bulletinId, viewNumberCookie);

        BulletinResponseDto bulletinResponse = bulletinService.findOne(bulletinId, viewCookie.isIncreaseNumberViews());

        PageResponseDto<CommentResponseDto> commentResponse = commentService.findCommentPageByBulletinId(bulletinId, 1, 10);

        model.addAttribute("bulletin", bulletinResponse);
        model.addAttribute("commentPage", commentResponse);

        response.addCookie(viewCookie.getCookie());
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
