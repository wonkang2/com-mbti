package com.commbti.domain.board.service;

import com.commbti.domain.board.dto.BoardListResponseDto;
import com.commbti.domain.board.entity.Board;
import com.commbti.domain.board.repository.BoardRepository;
import com.commbti.domain.file.service.FileService;
import com.commbti.domain.member.entity.MbtiType;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class NoticeBoardServiceTest {

    @InjectMocks
    private NoticeBoardService noticeBoardService;
    @Mock
    private BoardRepository boardRepository;

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

            List<BoardListResponseDto> result = noticeBoardService.findBoardListPage(page, size);
            assertThat(result.size()).isEqualTo(size);
        }

        @Test
        @DisplayName("게시글이 존재하지 않을 때 목록을 요청했을 때 확인")
        void 게시글_목록_조회_존재x_테스트() {

            List<Board> boardList = new ArrayList<>();

            int page = 1;
            int size = 10;

            given(boardRepository.findAllByPage(any())).willReturn(boardList);


            List<BoardListResponseDto> result = noticeBoardService.findBoardListPage(page, size);


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

            List<BoardListResponseDto> result = noticeBoardService.findBoardListPageByMbit(page, size, mbtiType);

            assertThat(result.size()).isEqualTo(boardList.size());
        }
        @Test
        @DisplayName("목록 결과가 존재하지 않을 때 조회가 안되는지 확인")
        void MBTI별_게시글_목록_조회_존재x_테스트() {

            List<Board> boardList = new ArrayList<>();

            int page = 1;
            int size = 10;

            given(boardRepository.findPageByMbti(any(), any())).willReturn(boardList);

            List<BoardListResponseDto> result = noticeBoardService.findBoardListPageByMbit(page, size, MbtiType.ENFJ);

            assertThat(result.size()).isEqualTo(boardList.size());
        }
    }
}
