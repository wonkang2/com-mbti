package com.commbti.domain.bulletinboard.service;

import com.commbti.domain.bulletinboard.dto.BoardResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
import com.commbti.domain.member.entity.MbtiType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BulletinBoardRepository bulletinBoardRepository;


    // 게시글 조회(전체)
    public List<BoardResponseDto> findBoardListPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        List<Bulletin> bulletinList = bulletinBoardRepository.findAllByPage(pageRequest);

        List<BoardResponseDto> response = bulletinList.stream().map(article -> article.toBoardResponseDto())
                .collect(Collectors.toList());

        return response;
    }

    // 게시글 조회(mbti별)
    public List<BoardResponseDto> findBoardListPageByMbit(int page, int size, MbtiType mbti) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Bulletin> bulletinList = bulletinBoardRepository.findPageByMbti(pageRequest, mbti);

        return bulletinList.stream().map(article -> article.toBoardResponseDto()).collect(Collectors.toList());
    }




}
