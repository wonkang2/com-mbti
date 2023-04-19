package com.commbti.domain.bulletinboard.service;

import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
import com.commbti.global.page.PageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BulletinBoardRepository bulletinBoardRepository;

    // 게시글 조회(전체) & 검색
    public PageResponseDto<BulletinResponseDto> findBulletinPage(String type, String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Bulletin> bulletinPage;
        if (type.equals("none") && keyword.equals("none")) {
            bulletinPage = bulletinBoardRepository.findPageWithMemberMbti(pageRequest);
        } else {
            if (type.equals("title")) {
                bulletinPage = bulletinBoardRepository.findByTitleContaining(keyword, pageRequest);
            } else if (type.equals("content")) {
                bulletinPage = bulletinBoardRepository.findByContentContaining(keyword, pageRequest);
            } else {
                throw new IllegalArgumentException("잘못된 접근입니다.");
            }
        }
        PageResponseDto<BulletinResponseDto> response = PageResponseDto.toBulletinPage(bulletinPage);

        return response;
    }
}
