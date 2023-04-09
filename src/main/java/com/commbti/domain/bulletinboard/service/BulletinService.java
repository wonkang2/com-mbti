package com.commbti.domain.bulletinboard.service;

import com.commbti.domain.bulletinboard.dto.BulletinPatchDto;
import com.commbti.domain.bulletinboard.dto.BulletinPostDto;
import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
import com.commbti.domain.file.service.ImageFileService;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BulletinService {

    private final BulletinBoardRepository bulletinBoardRepository;
    private final MemberService memberService;
    private final ImageFileService imageFileService;

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

        if (member.getId() != findBulletin.getMember().getId()) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        List<Long> deleteFiles = bulletinPatchDto.getDeleteFiles();
        List<MultipartFile> uploadFiles = bulletinPatchDto.getFiles();
        imageFileService.updateFiles(findBulletin, deleteFiles, uploadFiles);

        findBulletin.update(bulletinPatchDto.getTitle(), bulletinPatchDto.getContent());

        return findBulletin.getId();
    }

    // 게시글 상세 조회
    // 게시글을 조회할 때 해당 게시글의 viewCount가 수정되므로 @transaction(readOnly = true) 사용 불가
    public BulletinResponseDto findOne(Long bulletinId) {
        Bulletin findBulletin = getVerifiedBulletin(bulletinId);

        // TODO: 새로고침 및 의도적으로 조회수를 올리는 문제를 해결해야 함.
        findBulletin.addViewCount();

        return findBulletin.toBulletinResponseDto();
    }


    // 게시글 삭제
    public boolean deleteOne(Member member, Long bulletinId) {
        Bulletin findBulletin = getVerifiedBulletin(bulletinId);

        if (member.getId() != findBulletin.getMember().getId()) {
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
