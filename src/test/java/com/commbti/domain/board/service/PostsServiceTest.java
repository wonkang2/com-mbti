package com.commbti.domain.board.service;

import com.commbti.domain.board.dto.BoardPatchDto;
import com.commbti.domain.board.dto.BoardPostDto;
import com.commbti.domain.board.dto.BoardResponseDto;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class PostsServiceTest {

    @InjectMocks
    private PostsService postsService;

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private FileService fileService;

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

            postsService.createBoard(memberId, boardPostDto);
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

            postsService.updateBoard(memberId, boardId, boardPatchDto);

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

            postsService.updateBoard(memberId, boardId, boardPatchDto);

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

            postsService.updateBoard(memberId, boardId, boardPatchDto);

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

            postsService.updateBoard(memberId, boardId, boardPatchDto);

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
                            () -> postsService.updateBoard(fakeMemberId, boardId, boardPatchDto))
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
                            () -> postsService.updateBoard(fakeMemberId, fakeBoardId, fakeBoardPatchDto))
                    .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("해당 게시글이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("게시물 조회 테스트")
    class FindBoardTest {

        @DisplayName("게시글을 조회하였을 때 제대로 조회되는지 확인")
        @Test
        void 게시물_조회_테스트() {
            Long memberId = 1L;
            String nickname = "사용자A";
            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);
            ReflectionTestUtils.setField(member, "nickname", nickname);
            member.updateMbtiType(MbtiType.INTJ);

            Long boardId = 1L;
            Board savedBoard = Board.createBoard("테스트제목", "테스트내용", "/test.jpg", member);
            LocalDateTime createdAt = LocalDateTime.now();
            ReflectionTestUtils.setField(savedBoard, "createdAt", createdAt);

            given(boardRepository.findById(boardId)).willReturn(Optional.of(savedBoard));

            BoardResponseDto result = postsService.findBoard(boardId);

            assertThat(result.getTitle()).isEqualTo(savedBoard.getTitle());
            assertThat(result.getContent()).isEqualTo(savedBoard.getContent());
            assertThat(result.getFilePath()).isEqualTo(savedBoard.getFilePath());
            assertThat(result.getNickname()).isEqualTo(savedBoard.getMember().getNickname());
            assertThat(result.getMbtiType()).isEqualTo(savedBoard.getMember().getMbtiType());
            assertThat(result.getCreatedAt()).isEqualTo(savedBoard.getCreatedAt());
        }

        @DisplayName("존재하지 않는 게시글을 조회하였을 때 예외가 발생하는지 확인")
        @Test
        void 존재하지_않는_게시물_조회_테스트() {
            Long boardId = 1L;

            given(boardRepository.findById(boardId)).willReturn(Optional.ofNullable(null));


            assertThatThrownBy(() -> {
                postsService.findBoard(boardId);
            }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("존재하지 않는 게시글입니다.");
        }
    }

    @Nested
    @DisplayName("게시물 삭제 테스트")
    class DeleteBoardTest {

        @Test
        @DisplayName("게시물이 정상적으로 삭제되는지 확인")
        void 게시물_삭제_테스트() {
            Long memberId = 1L;
            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);

            Long boardId = 1L;
            Board savedBoard = Board.createBoard("테스트제목", "테스트내용", "/test.jpg", member);

            given(boardRepository.findById(boardId)).willReturn(Optional.of(savedBoard));
            willDoNothing().given(boardRepository).delete(savedBoard);

            boolean result = postsService.deleteBoard(memberId, boardId);

            assertThat(result).isTrue();
        }
        @Test
        @DisplayName("존재하지 않는 게시물을 삭제 요청하였을 때 예외가 발생하는지 확인")
        void 존재하지_않는_게시물_삭제_테스트() {
            Long memberId = 1L;
            Long fakeBoardId = 100L;
            given(boardRepository.findById(fakeBoardId)).willReturn(Optional.ofNullable(null));

            assertThatThrownBy(() -> {
                postsService.deleteBoard(memberId, fakeBoardId);
            }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("존재하지 않는 게시글입니다.");
        }
        @Test
        @DisplayName("권한없는 유저가 삭제 요청하였을 때 예외가 발생하는지 확인")
        void 권한없는_유저_게시물_삭제_테스트() {
            Long memberId = 1L;
            Long fakeMemberId = 100L;
            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);

            Long boardId = 1L;
            Board savedBoard = Board.createBoard("테스트제목", "테스트내용", "/test.jpg", member);

            given(boardRepository.findById(boardId)).willReturn(Optional.of(savedBoard));

            assertThatThrownBy(() -> {
                postsService.deleteBoard(fakeMemberId, boardId);
            }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("잘못된 접근입니다.");
        }
    }
}
