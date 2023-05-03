package com.commbti.domain.admin.service;

import com.commbti.domain.admin.dto.AdminBulletinResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
import com.commbti.global.page.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AdminBulletinService {
    private final BulletinBoardRepository bulletinBoardRepository;

    public PageResponseDto<AdminBulletinResponseDto> findBulletinPage(String type, String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        pageRequest.withSort(Sort.by(Sort.Order.asc("id")));
        Page<Bulletin> boardPage;
        if (type.equals("none") && type.equals("none")) {
            boardPage = bulletinBoardRepository.findAll(pageRequest);
        } else if (type.equals("title")) {
            boardPage = bulletinBoardRepository.findByTitleContains(pageRequest, keyword);
        } else if (type.equals("content")) {
            boardPage = bulletinBoardRepository.findByContentContains(pageRequest, keyword);
        } else if (type.equals("memberId")) {
            boardPage = bulletinBoardRepository.findByMember_id(pageRequest, Long.parseLong(keyword));
        } else if (type.equals("username")) {
            boardPage = bulletinBoardRepository.findByMember_usernameContains(pageRequest, keyword);
        } else if (type.equals("id")) {
            boardPage = bulletinBoardRepository.findById(pageRequest, Long.parseLong(keyword));
        } else {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }


        PageResponseDto<AdminBulletinResponseDto> response = PageResponseDto.toAdminBulletinPage(boardPage);


        return response;
    }

    public void delete(Long bulletinId) {
        Bulletin foundBulletin = bulletinBoardRepository.findById(bulletinId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
        bulletinBoardRepository.delete(foundBulletin);
    }


}
