package com.commbti.domain.board.service;

import com.commbti.domain.board.dto.BoardListResponseDto;
import com.commbti.domain.board.entity.Board;
import com.commbti.domain.board.repository.BoardRepository;
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
public class NoticeBoardService {

    private final BoardRepository boardRepository;


    // 게시글 조회(전체)
    public List<BoardListResponseDto> findBoardListPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        List<Board> boardList = boardRepository.findAllByPage(pageRequest);

        List<BoardListResponseDto> response = boardList.stream().map(board -> board.toListResponseDto())
                .collect(Collectors.toList());

        return response;
    }

    // 게시글 조회(mbti별)
    public List<BoardListResponseDto> findBoardListPageByMbit(int page, int size, MbtiType mbti) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Board> boardList = boardRepository.findPageByMbti(pageRequest, mbti);

        return boardList.stream().map(board -> board.toListResponseDto()).collect(Collectors.toList());
    }




}
