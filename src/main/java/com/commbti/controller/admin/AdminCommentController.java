package com.commbti.controller.admin;

import com.commbti.domain.admin.dto.AdminCommentResponseDto;
import com.commbti.domain.admin.service.AdminCommentService;
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
@Controller
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private final AdminCommentService adminCommentService;

    @GetMapping
    public String getAdminCommentPage(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       Model model) {
        PageResponseDto<AdminCommentResponseDto> commentPage = adminCommentService.findCommentPage(page, size);
        model.addAttribute("commentPage", commentPage);
        return "admin/admin-comment";
    }

    @GetMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId,
                                HttpServletRequest httpServletRequest) {
        adminCommentService.deleteOne(commentId);

        String referer = httpServletRequest.getHeader("Referer");

        return "redirect:" + referer;
    }
}
