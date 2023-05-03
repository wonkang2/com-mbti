package com.commbti.domain.bulletinboard.service;

import com.commbti.domain.bulletinboard.dto.BoardResponseDto;
import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
import com.commbti.global.exception.ExceptionCode;
import com.commbti.global.exception.ViewException;
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
        Page<BoardResponseDto> boardDtoPage;
        if (type.equals("none") && keyword.equals("none")) {
            boardDtoPage = bulletinBoardRepository.findBoard(pageRequest);
        } else {
            if (type.equals("title")) {
                boardDtoPage = bulletinBoardRepository.findBoardByTitleContains(pageRequest, keyword);
            } else if (type.equals("content")) {
                boardDtoPage = bulletinBoardRepository.findBoardByContentContains(pageRequest, keyword);
            } else {
                throw new ViewException(ExceptionCode.INVALID_PAGE_REQUEST);
            }
        }
        PageResponseDto<BulletinResponseDto> response = PageResponseDto.toBulletinPage(boardDtoPage);

        return response;
    }
}
