package com.commbti.controller.api;

import com.commbti.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthApiController {
    private final MemberService memberService;

    @GetMapping("/check/usernames")
    public ResponseEntity checkDuplicateUsername(@RequestParam("username") String username) {
        memberService.checkDuplicateUsername(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check/emails")
    public ResponseEntity checkVerifiedEmail(@RequestParam("email") String email) {
        String authNumber = memberService.verifyEmail(email);
        Map<String, String> response = new HashMap<>();
        response.put("authNumber", authNumber);

        return ResponseEntity.ok(response);
    }
}
