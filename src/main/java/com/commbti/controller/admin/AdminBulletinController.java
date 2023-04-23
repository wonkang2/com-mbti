package com.commbti.controller.admin;

import com.commbti.domain.admin.dto.AdminBulletinResponseDto;
import com.commbti.domain.admin.service.AdminBulletinService;
import com.commbti.domain.comment.service.CommentService;
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
@RequestMapping("/admin/bulletins")
@Controller
public class AdminBulletinController {
    private final AdminBulletinService adminBulletinService;
    private final CommentService commentService;

    @GetMapping
    public String getAdminBulletinPage(@RequestParam(value = "type", defaultValue = "none") String type,
                                       @RequestParam(value = "keyword", defaultValue = "none") String keyword,
                                       @RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       Model model) {
        PageResponseDto<AdminBulletinResponseDto> bulletinPage = adminBulletinService.findBulletinPage(type, keyword, page, size);

        model.addAttribute("bulletinPage", bulletinPage);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "admin/admin-bulletin";
    }

    @GetMapping("/{bulletinId}/delete")
    public String deleteBulletin(@PathVariable Long bulletinId,
                                 HttpServletRequest httpServletRequest) {
        commentService.deleteAllByBulletinId(bulletinId);
        adminBulletinService.delete(bulletinId);

        String referer = httpServletRequest.getHeader("Referer");
        return "redirect:" + referer;
    }
}
