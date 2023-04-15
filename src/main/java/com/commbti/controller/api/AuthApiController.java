package com.commbti.controller.api;

import com.commbti.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AuthApiController {
    private final MemberService memberService;

    @GetMapping("/auth/check/usernames")
    public ResponseEntity checkDuplicateUsername(@RequestParam("username") String username) {
        memberService.checkDuplicateUsername(username);
        return ResponseEntity.ok().build();
    }

}
