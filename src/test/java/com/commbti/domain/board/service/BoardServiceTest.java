package com.commbti.domain.board.service;

import com.commbti.domain.board.dto.BoardListResponseDto;
import com.commbti.domain.board.dto.BoardPatchDto;
import com.commbti.domain.board.dto.BoardPostDto;
import com.commbti.domain.board.entity.Board;
import com.commbti.domain.board.repository.BoardRepository;
import com.commbti.domain.file.service.FileService;
import com.commbti.domain.member.entity.MbtiType;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private FileService fileService;

    @Mock
    private MemberService memberService;

    @Nested
    @DisplayName("게시글 등록")
    class CreateBoardTest {
        @Test
        @DisplayName("게시글이 정상적으로 등록되는지 확인")
        void createBoardTest() {
            Long memberId = 1L;
            Member member = new Member();
            String title = "test";
            String content = "test";
            MockMultipartFile multipartFile = new MockMultipartFile("테스트", "text.jpg", "image/jpg", "test".getBytes());
            String filePath = "/filePath";
            BoardPostDto boardPostDto = new BoardPostDto(title, content, multipartFile);

            given(memberService.findMember(memberId)).willReturn(member);
            given(fileService.upload(multipartFile)).willReturn(filePath);
            willDoNothing().given(boardRepository).save(any());

            boardService.createBoard(memberId, boardPostDto);
        }
    }

    @Nested
    @DisplayName("게시글 수정 테스트")
    class UpdateBoardTest {
        @Test
        @DisplayName("게시글이 정상적으로 수정되는지 확인 - 제목, 내용, 이미지")
        void 모든_내용_수정_테스트() {
            Long memberId = 1L;
            Long boardId = 1L;

            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);

            String title = "기존제목";
            String content = "기존내용";
            String filePath = "/filePath";
            Board findBoard = Board.createBoard(title, content, filePath, member);

            String newTitle = "수정제목";
            String newContent = "수정내용";
            MockMultipartFile newMultipartFile = new MockMultipartFile("수정이미지", "new.jpg", "image/jpg", "test".getBytes());
            String newFilePath = "/filePath";
            BoardPatchDto boardPatchDto = new BoardPatchDto(newTitle, newContent, newMultipartFile);

            given(boardRepository.findById(boardId)).willReturn(Optional.of(findBoard));
            given(fileService.update(findBoard.getFilePath(), boardPatchDto.getFile())).willReturn(newFilePath);

            boardService.updateBoard(memberId, boardId, boardPatchDto);

            assertThat(findBoard.getTitle()).isEqualTo(newTitle);
            assertThat(findBoard.getContent()).isEqualTo(newContent);
            assertThat(findBoard.getFilePath()).isEqualTo(newFilePath);
        }

        @Test
        @DisplayName("게시글이 정상적으로 수정되는지 확인 - 제목")
        void 제목_수정_테스트() {
            Long memberId = 1L;
            Long boardId = 1L;

            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);

            String title = "기존제목";
            String content = "기존내용";
            String filePath = "/filePath";
            Board findBoard = Board.createBoard(title, content, filePath, member);

            String newTitle = "수정제목";
            BoardPatchDto boardPatchDto = new BoardPatchDto(newTitle, null, null);

            given(boardRepository.findById(boardId)).willReturn(Optional.of(findBoard));
            given(fileService.update(findBoard.getFilePath(), boardPatchDto.getFile())).willReturn(null);

            boardService.updateBoard(memberId, boardId, boardPatchDto);

            assertThat(findBoard.getTitle()).isEqualTo(newTitle);
            assertThat(findBoard.getContent()).isEqualTo(content);
            assertThat(findBoard.getFilePath()).isEqualTo(filePath);
        }

        @Test
        @DisplayName("게시글이 정상적으로 수정되는지 확인 - 내용")
        void 내용_수정_테스트() {
            Long memberId = 1L;
            Long boardId = 1L;

            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);

            String title = "기존제목";
            String content = "기존내용";
            String filePath = "/filePath";
            Board findBoard = Board.createBoard(title, content, filePath, member);

            String newContent = "수정내용";
            BoardPatchDto boardPatchDto = new BoardPatchDto(null, newContent, null);

            given(boardRepository.findById(boardId)).willReturn(Optional.of(findBoard));
            given(fileService.update(findBoard.getFilePath(), boardPatchDto.getFile())).willReturn(null);

            boardService.updateBoard(memberId, boardId, boardPatchDto);

            assertThat(findBoard.getTitle()).isEqualTo(title);
            assertThat(findBoard.getContent()).isEqualTo(newContent);
            assertThat(findBoard.getFilePath()).isEqualTo(filePath);
        }

        @Test
        @DisplayName("게시글이 정상적으로 수정되는지 확인 - 이미지")
        void 이미지_수정_테스트() {
            Long memberId = 1L;
            Long boardId = 1L;

            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);

            String title = "기존제목";
            String content = "기존내용";
            String filePath = "/filePath";
            Board findBoard = Board.createBoard(title, content, filePath, member);

            MockMultipartFile newMultipartFile = new MockMultipartFile("수정이미지", "new.jpg", "image/jpg", "test".getBytes());
            String newFilePath = "/filePath";
            BoardPatchDto boardPatchDto = new BoardPatchDto(null, null, newMultipartFile);

            given(boardRepository.findById(boardId)).willReturn(Optional.of(findBoard));
            given(fileService.update(findBoard.getFilePath(), boardPatchDto.getFile())).willReturn(newFilePath);

            boardService.updateBoard(memberId, boardId, boardPatchDto);

            assertThat(findBoard.getTitle()).isEqualTo(title);
            assertThat(findBoard.getContent()).isEqualTo(content);
            assertThat(findBoard.getFilePath()).isEqualTo(newFilePath);
        }

        @Test
        @DisplayName("권한이 없는 유저가 수정할 때 예외가 발생하는지 확인")
        void 권한없는_유저_수정_테스트() {
            Long memberId = 1L;
            Long fakeMemberId = 100L;
            Long boardId = 1L;

            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);

            String title = "기존제목";
            String content = "기존내용";
            String filePath = "/filePath";
            Board findBoard = Board.createBoard(title, content, filePath, member);

            String newTitle = "수정제목";
            String newContent = "수정내용";
            MockMultipartFile newMultipartFile = new MockMultipartFile("수정이미지", "new.jpg", "image/jpg", "test".getBytes());
            String newFilePath = "/filePath";
            BoardPatchDto boardPatchDto = new BoardPatchDto(newTitle, newContent, newMultipartFile);

            given(boardRepository.findById(boardId)).willReturn(Optional.of(findBoard));

            Assertions.assertThatThrownBy(
                            () -> boardService.updateBoard(fakeMemberId, boardId, boardPatchDto))
                    .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("잘못된 접근입니다.");
        }

        @Test
        @DisplayName("존재하지 않는 게시글을 수정 요청하였을 때 예외가 발생하는지 확인")
        void 존재하지_않는_게시글_수정_테스트() {
            Long fakeBoardId = 100L;
            Long fakeMemberId = 100L;

            BoardPatchDto fakeBoardPatchDto = new BoardPatchDto(null, null, null);
            given(boardRepository.findById(fakeBoardId)).willReturn(Optional.ofNullable(null));

            Assertions.assertThatThrownBy(
                            () -> boardService.updateBoard(fakeMemberId, fakeBoardId, fakeBoardPatchDto))
                    .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("해당 게시글이 존재하지 않습니다.");
        }
    }


    @Nested
    @DisplayName("게시글 목록 페이지 조회")
    class FindBoardListTest {
        @Test
        @DisplayName("게시글 목록 조회가 정상적으로 동작하는지 확인")
        void 게시글_목록_정상_조회_테스트() {
            Long memberId1 = 1L;
            Long memberId2 = 2L;

            Member member1 = new Member();
            Member member2 = new Member();

            ReflectionTestUtils.setField(member1, "id", memberId1);
            ReflectionTestUtils.setField(member2, "id", memberId2);

            Board board1 = Board.createBoard("테스트제목1", "테스트내용1", "/test1.jpg", member1);
            Board board2 = Board.createBoard("테스트제목2", "테스트내용2", "/test2.jpg", member2);

            List<Board> boardList = List.of(board1, board2);

            int page = 1;
            int size = 2;

            given(boardRepository.findAllByPage(any())).willReturn(boardList);

            List<BoardListResponseDto> result = boardService.findBoardListPage(page, size);
            assertThat(result.size()).isEqualTo(size);
        }

        @Test
        @DisplayName("게시글이 존재하지 않을 때 목록을 요청했을 때 확인")
        void 게시글_목록_조회_존재x_테스트() {

            List<Board> boardList = new ArrayList<>();

            int page = 1;
            int size = 10;

            given(boardRepository.findAllByPage(any())).willReturn(boardList);


            List<BoardListResponseDto> result = boardService.findBoardListPage(page, size);


            assertThat(result.size()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("MBTI별 게시글 목록 조회 테스트")
    class FindBoardListByMbti {
        @Test
        @DisplayName("게시글 목록 조회가 정상적으로 동작하는지 확인")
        void MBTI별_게시글_목록_조회_테스트() {
            Long memberId1 = 1L;
            Long memberId2 = 2L;
            Long memberId3 = 3L;

            Member member1 = new Member();
            Member member2 = new Member();
            Member member3 = new Member();

            ReflectionTestUtils.setField(member1, "id", memberId1);
            ReflectionTestUtils.setField(member2, "id", memberId2);
            ReflectionTestUtils.setField(member3, "id", memberId3);

            member1.updateMbtiType(MbtiType.INTJ);
            member2.updateMbtiType(MbtiType.INTJ);
            member3.updateMbtiType(MbtiType.INTJ);

            Board board1 = Board.createBoard("테스트1", "테스트1", "테스트1", member1);
            Board board2 = Board.createBoard("테스트2", "테스트1", "테스트1", member1);
            Board board3 = Board.createBoard("테스트3", "테스트1", "테스트1", member2);
            Board board4 = Board.createBoard("테스트4", "테스트1", "테스트1", member3);

            List<Board> boardList = List.of(board1, board2, board3, board4);

            int page = 1;
            int size = 10;
            MbtiType mbtiType = member1.getMbtiType();
            given(boardRepository.findPageByMbti(any(), eq(mbtiType))).willReturn(boardList);

            List<BoardListResponseDto> result = boardService.findBoardListPageByMbit(page, size, mbtiType);

            assertThat(result.size()).isEqualTo(boardList.size());
        }
        @Test
        @DisplayName("목록 결과가 존재하지 않을 때 조회가 안되는지 확인")
        void MBTI별_게시글_목록_조회_존재x_테스트() {

            List<Board> boardList = new ArrayList<>();

            int page = 1;
            int size = 10;

            given(boardRepository.findPageByMbti(any(), any())).willReturn(boardList);

            List<BoardListResponseDto> result = boardService.findBoardListPageByMbit(page, size, MbtiType.ENFJ);

            assertThat(result.size()).isEqualTo(boardList.size());
        }
    }
}
