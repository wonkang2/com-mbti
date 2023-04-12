package com.commbti.controller.api;

import com.commbti.domain.member.dto.MemberPatchDto;
import com.commbti.domain.member.dto.MemberSignupDto;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
}
