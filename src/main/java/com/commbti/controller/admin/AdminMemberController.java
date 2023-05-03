package com.commbti.controller.admin;

import com.commbti.domain.admin.dto.AdminCommentResponseDto;
import com.commbti.domain.admin.service.AdminMemberService;
import com.commbti.global.page.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminMemberController {
    private final AdminMemberService adminMemberService;

    @GetMapping
    public String getAdminPage() {
        return "redirect:/admin/members";
    }

    @GetMapping("/members")
    public String getAdminMemberPage(@RequestParam(defaultValue = "none") String type,
                                     @RequestParam(defaultValue = "none") String keyword,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     Model model) {

        PageResponseDto<AdminCommentResponseDto> memberPage = adminMemberService.findMemberPage(type, keyword, page, size);
        model.addAttribute("memberPage", memberPage);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "admin/admin-member";
    }


    @GetMapping("/members/{memberId}/block")
    public String block(@PathVariable Long memberId,
                        HttpServletRequest request) {
        adminMemberService.updateBlockStatus(memberId);
        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }
}
