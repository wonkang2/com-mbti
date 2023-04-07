package com.commbti.controller.api;

import com.commbti.domain.member.dto.MemberSignupDto;
import com.commbti.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity register(@RequestBody MemberSignupDto request) {
        Long signup = memberService.save(request);
        URI locationPath = URI.create("/login");
        return ResponseEntity.created(locationPath).build();
    }
}
