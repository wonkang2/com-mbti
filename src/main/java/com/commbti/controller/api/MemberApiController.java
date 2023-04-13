package com.commbti.controller.api;

import com.commbti.domain.member.dto.MemberPatchDto;
import com.commbti.domain.member.dto.MemberResponseDto;
import com.commbti.domain.member.dto.MemberSignupDto;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity register(@RequestBody MemberSignupDto request) {
        Long signup = memberService.signup(request);
        URI locationPath = URI.create("/login");
        return ResponseEntity.created(locationPath).build();
    }

    @PatchMapping("/members/{memberId}")
    public ResponseEntity patch(@AuthenticationPrincipal Member member,
                                @PathVariable Long memberId,
                                @RequestBody MemberPatchDto request) {
        memberService.modify(member, memberId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members")
    public ResponseEntity getUsername(@RequestParam("email") String email) {
        log.info("아이디 찾기 호출 - email: {}", email);
        MemberResponseDto response = memberService.findOneByEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
