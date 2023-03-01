package com.commbti.domain.bulletinboard.service;

import com.commbti.domain.bulletinboard.dto.BoardResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
import com.commbti.domain.member.entity.MbtiType;
import com.commbti.domain.member.entity.Member;
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
public class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;
    @Mock
    private BulletinBoardRepository bulletinBoardRepository;

    @Nested
    @DisplayName("게시글 목록 페이지 조회")
    class FindBulletinListTest {
        @Test
        @DisplayName("게시글 목록 조회가 정상적으로 동작하는지 확인")
        void 게시글_목록_정상_조회_테스트() {
            Long memberId1 = 1L;
            Long memberId2 = 2L;

            Member member1 = new Member();
            Member member2 = new Member();

            ReflectionTestUtils.setField(member1, "id", memberId1);
            ReflectionTestUtils.setField(member2, "id", memberId2);

            Bulletin bulletin1 = Bulletin.createArticle("테스트제목1", "테스트내용1", "/test1.jpg", member1);
            Bulletin bulletin2 = Bulletin.createArticle("테스트제목2", "테스트내용2", "/test2.jpg", member2);

            List<Bulletin> bulletinList = List.of(bulletin1, bulletin2);

            int page = 1;
            int size = 2;

            given(bulletinBoardRepository.findAllByPage(any())).willReturn(bulletinList);

            List<BoardResponseDto> result = boardService.findBoardListPage(page, size);
            assertThat(result.size()).isEqualTo(size);
        }

        @Test
        @DisplayName("게시글이 존재하지 않을 때 목록을 요청했을 때 확인")
        void 게시글_목록_조회_존재x_테스트() {

            List<Bulletin> bulletinList = new ArrayList<>();

            int page = 1;
            int size = 10;

            given(bulletinBoardRepository.findAllByPage(any())).willReturn(bulletinList);


            List<BoardResponseDto> result = boardService.findBoardListPage(page, size);


            assertThat(result.size()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("MBTI별 게시글 목록 조회 테스트")
    class FindBulletinListByMbti {
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

            Bulletin bulletin1 = Bulletin.createArticle("테스트1", "테스트1", "테스트1", member1);
            Bulletin bulletin2 = Bulletin.createArticle("테스트2", "테스트1", "테스트1", member1);
            Bulletin bulletin3 = Bulletin.createArticle("테스트3", "테스트1", "테스트1", member2);
            Bulletin bulletin4 = Bulletin.createArticle("테스트4", "테스트1", "테스트1", member3);

            List<Bulletin> bulletinList = List.of(bulletin1, bulletin2, bulletin3, bulletin4);

            int page = 1;
            int size = 10;
            MbtiType mbtiType = member1.getMbtiType();
            given(bulletinBoardRepository.findPageByMbti(any(), eq(mbtiType))).willReturn(bulletinList);

            List<BoardResponseDto> result = boardService.findBoardListPageByMbit(page, size, mbtiType);

            assertThat(result.size()).isEqualTo(bulletinList.size());
        }
        @Test
        @DisplayName("목록 결과가 존재하지 않을 때 조회가 안되는지 확인")
        void MBTI별_게시글_목록_조회_존재x_테스트() {

            List<Bulletin> bulletinList = new ArrayList<>();

            int page = 1;
            int size = 10;

            given(bulletinBoardRepository.findPageByMbti(any(), any())).willReturn(bulletinList);

            List<BoardResponseDto> result = boardService.findBoardListPageByMbit(page, size, MbtiType.ENFJ);

            assertThat(result.size()).isEqualTo(bulletinList.size());
        }
    }
}
