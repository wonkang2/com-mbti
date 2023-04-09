package com.commbti.controller.api;

import com.commbti.domain.bulletinboard.dto.BulletinPatchDto;
import com.commbti.domain.bulletinboard.dto.BulletinPostDto;
import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.service.BulletinService;
import com.commbti.domain.comment.dto.CommentPageDto;
import com.commbti.domain.comment.dto.CommentRequestDto;
import com.commbti.domain.comment.service.CommentService;
import com.commbti.domain.member.entity.Member;
import com.commbti.global.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/bulletins")
@RestController
public class BulletinApiController {

    private final BulletinService bulletinService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity post(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                               BulletinPostDto request) {
        Member loginMember = customUserDetails.getMember();

        Long bulletinId = bulletinService.createBulletin(loginMember, request);
        URI uri = URI.create("/bulletin-board/bulletins/" + bulletinId);

        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/{bulletin-id}")
    public ResponseEntity patch(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                @PathVariable("bulletin-id") Long bulletinId,
                                BulletinPatchDto bulletinPatchDto) {
        Member loginMember = customUserDetails.getMember();

        bulletinService.updateBulletin(loginMember, bulletinId, bulletinPatchDto);
        return ResponseEntity.ok().header("Location", "/bulletin-board/bulletins/" + bulletinId).build();
    }

    @DeleteMapping("/{bulletin-id}")
    public ResponseEntity delete(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                 @PathVariable("bulletin-id") Long bulletinId) {
        Member loginMember = customUserDetails.getMember();

        commentService.deleteAllByBulletinId(bulletinId);
        bulletinService.deleteOne(loginMember, bulletinId);

        return ResponseEntity.ok().header("Location", "/").build();
    }

}
