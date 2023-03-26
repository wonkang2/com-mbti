package com.commbti.domain.bulletinboard.service;

import com.commbti.domain.bulletinboard.dto.BoardResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
import com.commbti.domain.member.entity.MbtiType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BulletinBoardRepository bulletinBoardRepository;

    // 게시글 조회(전체)
    public List<BoardResponseDto> findBoardListPage(int page, int size) {
        log.trace("findBoardListPage 호출: page={}, size={}", page, size);
        // 기본값: page=0; size= 10;
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Bulletin> boardPage = bulletinBoardRepository.findPageWithMemberMbti(pageRequest);

        log.debug("boardPage.count = {}", boardPage.stream().count());
        log.debug("현재 저장되어 있는 bulletin의 개수: {}", bulletinBoardRepository.count());

        List<BoardResponseDto> response = boardPage.stream().map(article -> article.toBoardResponseDto())
                .collect(Collectors.toList());
        log.debug("response.size = {}", response.size());
        log.trace("findBoardListPage 정상");
        return response;
    }

    // 게시글 조회(mbti별)
//    public List<BoardResponseDto> findBoardListPageByMbit(int page, int size, MbtiType mbti) {
//        PageRequest pageRequest = PageRequest.of(page, size);
//        List<Bulletin> bulletinList = bulletinBoardRepository.findPageByMbti(pageRequest, mbti);
//
//        return bulletinList.stream().map(article -> article.toBoardResponseDto()).collect(Collectors.toList());
//    }




}
