package com.commbti.domain.bulletinboard.service;

import com.commbti.domain.bulletinboard.dto.BulletinPatchDto;
import com.commbti.domain.bulletinboard.dto.BulletinPostDto;
import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
import com.commbti.domain.file.service.ImageFileService;
import com.commbti.domain.member.entity.Member;
import com.commbti.global.event.BulletinViewEvent;
import com.commbti.global.exception.ApiException;
import com.commbti.global.exception.ExceptionCode;
import com.commbti.global.exception.ViewException;
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

    // 게시글 수정
    @Transactional
    public Long updateBulletin(Member member, Long bulletinId, BulletinPatchDto bulletinPatchDto) {
        Bulletin foundBulletin = getVerifiedBulletin(bulletinId);
        verifyAuthority(member, foundBulletin);
        List<Long> deleteFiles = bulletinPatchDto.getDeleteFiles();
        List<MultipartFile> uploadFiles = bulletinPatchDto.getFiles();
        imageFileService.updateFiles(foundBulletin, deleteFiles, uploadFiles);

        foundBulletin.update(bulletinPatchDto.getTitle(), bulletinPatchDto.getContent());

        return foundBulletin.getId();
    }


    /**
     * 게시글 상세보기로 View Count와 관련된 메서드
     */
    @Transactional(readOnly = true)
    public BulletinResponseDto findOne(Long bulletinId, boolean increaseNumberViews) {
        Bulletin foundBulletin = getVerifiedBulletinThrowViewException(bulletinId);
        if (increaseNumberViews) {
            eventPublisher.publishEvent(new BulletinViewEvent(foundBulletin));
        }
        return foundBulletin.toBulletinResponseDto();
    }
    /**
     * 단순 게시글에 대한 정보를 조회하기 위한 메서드
     */
    @Transactional(readOnly = true)
    public BulletinResponseDto findOne(Long bulletinId) {
        Bulletin foundBulletin = getVerifiedBulletinThrowViewException(bulletinId);
        return foundBulletin.toBulletinResponseDto();
    }


    // 게시글 삭제
    public void deleteOne(Member member, Long bulletinId) {
        Bulletin foundBulletin = getVerifiedBulletin(bulletinId);
        verifyAuthority(member, foundBulletin);
        bulletinBoardRepository.delete(foundBulletin);
    }

    /**
     * @return Bulletin Entity Or ApiException(HTTP Status 404)
     */
    @Transactional(readOnly = true)
    public Bulletin getVerifiedBulletin(Long bulletinId) {
        Optional<Bulletin> optionalBoard = bulletinBoardRepository.findById(bulletinId);
        Bulletin bulletin = optionalBoard.orElseThrow(
                () -> new ApiException(ExceptionCode.BULLETIN_NOT_FOUND));
        return bulletin;
    }

    /**
     * @return Bulletin Entity Or ViewException
     * View 에서 발생하는 Exception을 공통처리하기 위해 ViewException을 던지는 메서드 생성함.
     */
    @Transactional(readOnly = true)
    protected Bulletin getVerifiedBulletinThrowViewException(Long bulletinId) {
        Optional<Bulletin> optionalBoard = bulletinBoardRepository.findById(bulletinId);
        Bulletin bulletin = optionalBoard.orElseThrow(
                () -> new ViewException(ExceptionCode.BULLETIN_NOT_FOUND));
        return bulletin;
    }

    /**
     * Bulletin의 memberId와 요청한 Member의 Id가 일치하는지 검증하여 권한이 있는지 체크하는 메서드
     */
    protected void verifyAuthority(Member member, Bulletin foundBulletin) {
        if (!foundBulletin.getMember().getId().equals(member.getId())) {
            log.info("요청한 유저의 memberId와 bulletin의 memberId가 일치하지 않음. memberId:{}, bulletin.member.id:{}", member.getId(), foundBulletin.getMember().getId());
            throw new ApiException(ExceptionCode.FORBIDDEN);
        }
    }

}
