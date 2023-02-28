package com.commbti.domain.board.service;

import com.commbti.domain.board.dto.BoardPatchDto;
import com.commbti.domain.board.dto.BoardPostDto;
import com.commbti.domain.board.dto.BoardResponseDto;
import com.commbti.domain.board.entity.Board;
import com.commbti.domain.board.repository.BoardRepository;
import com.commbti.domain.file.service.FileService;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostsService {

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
    @Transactional
    public Long updateBoard(Long memberId, Long boardId, BoardPatchDto patchDto) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!(board.getMember().getId() == memberId)) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
        String filePath = fileService.update(board.getFilePath(), patchDto.getFile());

        board.update(patchDto.getTitle(), patchDto.getContent(), filePath);

        return board.getId();
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public BoardResponseDto findBoard(Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return board.toResponseDto();
    }

    // 게시글 삭제
    @Transactional
    public boolean deleteBoard(Long memberId, Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        if (!(board.getMember().getId() == memberId)) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
        boardRepository.delete(board);
        return true;
    }
}
