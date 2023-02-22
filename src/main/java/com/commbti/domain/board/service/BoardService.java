package com.commbti.domain.board.service;

import com.commbti.domain.board.dto.BoardListResponseDto;
import com.commbti.domain.board.dto.BoardPatchDto;
import com.commbti.domain.board.dto.BoardPostDto;
import com.commbti.domain.board.dto.BoardResponseDto;
import com.commbti.domain.board.entity.Board;
import com.commbti.domain.board.repository.BoardRepository;
import com.commbti.domain.file.service.FileService;
import com.commbti.domain.member.entity.MbtiType;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final FileService fileService;

    // 게시글 등록
    @Transactional
    public Long createBoard(Long memberId, BoardPostDto postDto) {
        Member member = memberService.findMember(memberId);

        MultipartFile file = postDto.getFile();
        String filePath = fileService.upload(file);

        Board board = Board.createBoard(postDto.getTitle(), postDto.getContent(), filePath, member);

        boardRepository.save(board);
        return board.getId();
    }
    // 게시글 수정
    public Long updateBoard(Long memberId, Long boardId, BoardPatchDto patchDto) {
        Member member = memberService.findMember(memberId);
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        String filePath = fileService.update(patchDto.getFile());

        board.update(patchDto.getTitle(), patchDto.getContent(), filePath);

        return board.getId();
    }
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

    // 게시글 상세 조회
    public BoardResponseDto findBoard(Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return board.toResponseDto();
    }

    // 게시글 삭제
}
