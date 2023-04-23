package com.commbti.domain.bulletinboard.service;

import com.commbti.domain.bulletinboard.dto.BulletinPatchDto;
import com.commbti.domain.bulletinboard.dto.BulletinPostDto;
import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
import com.commbti.domain.file.service.ImageFileService;
import com.commbti.domain.member.entity.Member;
import com.commbti.global.event.BulletinViewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BulletinService {

    private final BulletinBoardRepository bulletinBoardRepository;
    private final ImageFileService imageFileService;
    private final ApplicationEventPublisher eventPublisher;

    // 게시글 등록
    public Long createBulletin(Member member, BulletinPostDto postDto) {

        List<MultipartFile> uploadFiles = postDto.getFiles();

        Bulletin bulletin = Bulletin.createArticle(postDto.getTitle(), postDto.getContent(), member);
        bulletinBoardRepository.save(bulletin);

        imageFileService.uploadFiles(bulletin, uploadFiles);

        return bulletin.getId();
    }

    //    // 게시글 수정
    @Transactional
    public Long updateBulletin(Member member, Long bulletinId, BulletinPatchDto bulletinPatchDto) {
        Bulletin findBulletin = getVerifiedBulletin(bulletinId);

        if (!findBulletin.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        List<Long> deleteFiles = bulletinPatchDto.getDeleteFiles();
        List<MultipartFile> uploadFiles = bulletinPatchDto.getFiles();
        imageFileService.updateFiles(findBulletin, deleteFiles, uploadFiles);

        findBulletin.update(bulletinPatchDto.getTitle(), bulletinPatchDto.getContent());

        return findBulletin.getId();
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public BulletinResponseDto findOne(Long bulletinId, boolean increaseNumberViews) {
        log.info("게시글 상세 조회 호출");
        Bulletin foundBulletin = getVerifiedBulletin(bulletinId);
        if (increaseNumberViews == true) {
            eventPublisher.publishEvent(new BulletinViewEvent(foundBulletin));
        }
        log.info("게시글 상세 조회 종료");
        return foundBulletin.toBulletinResponseDto();
    }
    @Transactional(readOnly = true)
    public BulletinResponseDto findOne(Long bulletinId) {
        Bulletin findBulletin = getVerifiedBulletin(bulletinId);
        return findBulletin.toBulletinResponseDto();
    }


    // 게시글 삭제
    public boolean deleteOne(Member member, Long bulletinId) {
        Bulletin findBulletin = getVerifiedBulletin(bulletinId);

        if (!findBulletin.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        bulletinBoardRepository.delete(findBulletin);
        return true;
    }

    // 존재하는 게시글 가져오는 메서드
    @Transactional(readOnly = true)
    public Bulletin getVerifiedBulletin(Long bulletinId) {
        Optional<Bulletin> optionalBoard = bulletinBoardRepository.findById(bulletinId);
        Bulletin bulletin = optionalBoard.orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return bulletin;
    }

}
