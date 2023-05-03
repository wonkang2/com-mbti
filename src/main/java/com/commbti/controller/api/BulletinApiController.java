package com.commbti.controller.api;

import com.commbti.domain.bulletinboard.dto.BulletinPatchDto;
import com.commbti.domain.bulletinboard.dto.BulletinPostDto;
import com.commbti.domain.bulletinboard.service.BulletinService;
import com.commbti.domain.comment.service.CommentService;
import com.commbti.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/bulletins")
@RestController
public class BulletinApiController {

    private final BulletinService bulletinService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity post(@AuthenticationPrincipal Member loginMember,
                               @Valid BulletinPostDto request) {

        Long bulletinId = bulletinService.createBulletin(loginMember, request);
        URI uri = URI.create("/bulletin-board/bulletins/" + bulletinId);

        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/{bulletin-id}")
    public ResponseEntity patch(@AuthenticationPrincipal Member loginMember,
                                @Min (value = 1,message = "정상적인 요청이 아닙니다.") @PathVariable("bulletin-id") Long bulletinId,
                                @Valid BulletinPatchDto bulletinPatchDto) {

        bulletinService.updateBulletin(loginMember, bulletinId, bulletinPatchDto);
        return ResponseEntity.ok().header("Location", "/bulletin-board/bulletins/" + bulletinId).build();
    }

    @DeleteMapping("/{bulletin-id}")
    public ResponseEntity delete(@AuthenticationPrincipal Member loginMember,
                                 @Min (value = 1,message = "정상적인 요청이 아닙니다.") @PathVariable("bulletin-id") Long bulletinId) {

        commentService.deleteByBulletinId(bulletinId);
        bulletinService.deleteOne(loginMember, bulletinId);

        return ResponseEntity.ok().header("Location", "/").build();
    }

}
