package com.commbti.domain.bulletinboard.service;

import com.commbti.domain.bulletinboard.dto.BulletinPatchDto;
import com.commbti.domain.bulletinboard.dto.BulletinPostDto;
import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
import com.commbti.domain.file.service.FileService;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BulletinService {

    private final BulletinBoardRepository bulletinBoardRepository;
    private final MemberService memberService;
    private final FileService fileService;

    // 게시글 등록
    @Transactional
    public Long createBulletin(Long memberId, BulletinPostDto postDto) {
        Member member = memberService.findMember(memberId);

        MultipartFile file = postDto.getFile();
        String filePath = fileService.upload(file);

        Bulletin bulletin = Bulletin.createArticle(postDto.getTitle(), postDto.getContent(), filePath, member);

        bulletinBoardRepository.save(bulletin);
        return bulletin.getId();
    }

    // 게시글 수정
    @Transactional
    public Long updateBulletin(Long memberId, Long bulletinId, BulletinPatchDto patchDto) {
        Optional<Bulletin> optionalBoard = bulletinBoardRepository.findById(bulletinId);
        Bulletin bulletin = optionalBoard.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!(bulletin.getMember().getId() == memberId)) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
        String filePath = fileService.update(bulletin.getFilePath(), patchDto.getFile());

        bulletin.update(patchDto.getTitle(), patchDto.getContent(), filePath);

        return bulletin.getId();
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public BulletinResponseDto findOne(Long bulletinId) {
        Optional<Bulletin> optionalBoard = bulletinBoardRepository.findById(bulletinId);
        Bulletin bulletin = optionalBoard.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return bulletin.toBulletinResponseDto();
    }

    // 게시글 삭제
    @Transactional
    public boolean deleteOne(Long memberId, Long bulletinId) {
        Optional<Bulletin> optionalBoard = bulletinBoardRepository.findById(bulletinId);
        Bulletin bulletin = optionalBoard.orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        if (!(bulletin.getMember().getId() == memberId)) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
        bulletinBoardRepository.delete(bulletin);
        return true;
    }
}
