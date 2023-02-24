package com.commbti.domain.board.repository.impl;

import com.commbti.domain.board.entity.Board;
import com.commbti.domain.board.repository.BoardRepository;
import com.commbti.domain.member.entity.MbtiType;
import com.commbti.domain.member.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BoardRepositoryImplTest {

    @Autowired
    private EntityManager em;
    private BoardRepository boardRepository;

    @BeforeEach
    public void init() {
        boardRepository = new BoardRepositoryImpl(em);
    }

    @AfterEach
    void clear() {
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("해당하는 id를 가진 게시글이 조회되는지 확인")
    void 게시글_조회_테스트() {
        Member member1 = new Member();
        Board board1 = Board.createBoard("테스트1", "테스트1", "/test1", member1);
        Board board2 = Board.createBoard("테스트2", "테스트2", "/test2", member1);
        boardRepository.save(board1);
        boardRepository.save(board2);

        Optional<Board> optionalBoard = boardRepository.findById(board1.getId());
        assertThat(optionalBoard).isNotEmpty();
        assertThat(optionalBoard.get()).isEqualTo(board1);
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 조회했을 때 조회되지 않는지 확인")
    void 존재하지않는_게시글_조회_테스트() {
        Member member1 = new Member();
        Board board1 = Board.createBoard("테스트1", "테스트1", "/test1", member1);
        Board board2 = Board.createBoard("테스트2", "테스트2", "/test2", member1);
        boardRepository.save(board1);
        boardRepository.save(board2);


        Optional<Board> optionalBoard = boardRepository.findById(0L);


        assertThat(optionalBoard).isEmpty();
    }


    @Test
    @DisplayName("게시글 페이지가 제대로 조회되는지 확인")
    void 게시글_목록_조회_테스트() {
        Member member1 = new Member();
        Member member2 = new Member();
        Board board1 = Board.createBoard("테스트1", "테스트1", "/test1", member1);
        boardRepository.save(board1);
        Board board2 = Board.createBoard("테스트2", "테스트2", "/test2", member2);
        boardRepository.save(board2);
        Board board3 = Board.createBoard("테스트3", "테스트3", "/test3", member1);
        boardRepository.save(board3);

        PageRequest pageRequest = PageRequest.of(0, 20);


        List<Board> boardList = boardRepository.findAllByPage(pageRequest);


        assertThat(boardList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("게시글 페이지가 존재하지 않을 떄 0개의 값이 나오는지 확인")
    void 게시글_목록_조회_테스트1() {
        Member member1 = new Member();
        Member member2 = new Member();
        Board board1 = Board.createBoard("테스트1", "테스트1", "/test1", member1);
        boardRepository.save(board1);
        Board board2 = Board.createBoard("테스트2", "테스트2", "/test2", member2);
        boardRepository.save(board2);
        Board board3 = Board.createBoard("테스트3", "테스트3", "/test3", member1);
        boardRepository.save(board3);
        PageRequest pageRequest = PageRequest.of(1, 20);

        List<Board> boardList = boardRepository.findAllByPage(pageRequest);

        assertThat(boardList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 페이지가 최신순으로 조회되는지 확인")
    void 게시글_목록_정렬_테스트() {
        Member member1 = new Member();
        Member member2 = new Member();
        Board board1 = Board.createBoard("테스트1", "테스트1", "/test1", member1);
        boardRepository.save(board1);
        Board board2 = Board.createBoard("테스트2", "테스트2", "/test2", member2);
        boardRepository.save(board2);
        Board board3 = Board.createBoard("테스트3", "테스트3", "/test3", member1);
        boardRepository.save(board3);

        PageRequest pageRequest = PageRequest.of(0, 20);


        List<Board> boardList = boardRepository.findAllByPage(pageRequest);


        assertThat(boardList.get(0).getCreatedAt()).isAfter(boardList.get(1).getCreatedAt());
        assertThat(boardList.get(1).getCreatedAt()).isAfter(boardList.get(2).getCreatedAt());
    }

    @Test
    @DisplayName("mbti별로 게시글이 제대로 조회되는지 확인")
    void mbti필터_게시글_목록_조회_테스트() {
        Member member1 = new Member();
        member1.updateMbtiType(MbtiType.INTJ);
        Member member2 = new Member();
        member2.updateMbtiType(MbtiType.INTP);
        Board board1 = Board.createBoard("테스트1", "테스트1", "/test1", member1);
        boardRepository.save(board1);
        Board board2 = Board.createBoard("테스트2", "테스트2", "/test2", member2);
        boardRepository.save(board2);
        Board board3 = Board.createBoard("테스트3", "테스트3", "/test3", member1);
        boardRepository.save(board3);

        PageRequest pageRequest = PageRequest.of(0, 20);


        List<Board> boardList = boardRepository.findPageByMbti(pageRequest, member1.getMbtiType());


        assertThat(boardList).contains(board1, board3);
        assertThat(boardList).doesNotContain(board2);
    }

    @Test
    @DisplayName("mbti별로 게시글이 최신순으로 조회되는지 확인")
    void mbti필터_게시글_목록_정렬_테스트() {
        Member member1 = new Member();
        member1.updateMbtiType(MbtiType.INTJ);
        Member member2 = new Member();
        member2.updateMbtiType(MbtiType.INTP);
        Board board1 = Board.createBoard("테스트1", "테스트1", "/test1", member1);
        boardRepository.save(board1);
        Board board2 = Board.createBoard("테스트2", "테스트2", "/test2", member2);
        boardRepository.save(board2);
        Board board3 = Board.createBoard("테스트3", "테스트3", "/test3", member1);
        boardRepository.save(board3);

        PageRequest pageRequest = PageRequest.of(0, 20);


        List<Board> boardList = boardRepository.findPageByMbti(pageRequest, member1.getMbtiType());


        assertThat(boardList.get(0).getCreatedAt()).isAfter(boardList.get(1).getCreatedAt());
    }

    @Test
    @DisplayName("mbti별로 게시글이 페이징처리되서 조회되는지 확인1")
    void mbti필터_게시글_목록_페이지_조회_테스트1() {
        Member member1 = new Member();
        member1.updateMbtiType(MbtiType.INTJ);
        Member member2 = new Member();
        member2.updateMbtiType(MbtiType.INTP);
        for (int i = 0; i < 20; i+=2) {
            boardRepository.save(
                    Board.createBoard("테스트" + i, "테스트" + i, "/test" + i, member1));
            boardRepository.save(
                    Board.createBoard("테스트" + i+1, "테스트" + i+1, "/test" + i+1, member2));
        }

        PageRequest pageRequest = PageRequest.of(0, 10);


        List<Board> boardList = boardRepository.findPageByMbti(pageRequest, member1.getMbtiType());


        assertThat(boardList.size()).isEqualTo(10);
        assertThat(boardList.get(0).getCreatedAt()).isAfter(boardList.get(1).getCreatedAt());
        assertThat(boardList.get(0).getMember().getMbtiType()).isEqualTo(boardList.get(9).getMember().getMbtiType());
    }

    @Test
    @DisplayName("mbti별로 게시글이 페이징처리되서 조회되는지 확인2")
    void mbti필터_게시글_목록_페이지_조회_테스트2() {
        Member member1 = new Member();
        member1.updateMbtiType(MbtiType.INTJ);
        Member member2 = new Member();
        member2.updateMbtiType(MbtiType.INTP);
        for (int i = 0; i < 20; i+=2) {
            boardRepository.save(
                    Board.createBoard("테스트" + i, "테스트" + i, "/test" + i, member1));
            boardRepository.save(
                    Board.createBoard("테스트" + i+1, "테스트" + i+1, "/test" + i+1, member2));
        }

        PageRequest pageRequest = PageRequest.of(1, 10);


        List<Board> boardList = boardRepository.findPageByMbti(pageRequest, member1.getMbtiType());


        assertThat(boardList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("삭제 확인")
    void 삭제_테스트() {
        Member member1 = new Member();
        Board board1 = Board.createBoard("테스트1", "테스트1", "/test1", member1);
        boardRepository.save(board1);

        boardRepository.delete(board1);

        assertThat(boardRepository.findById(board1.getId())).isEmpty();
    }

    @Test
    @DisplayName("N+1 문제 확인")
    void n_plus_1_test() {
        Member member1 = new Member();
        member1.updateMbtiType(MbtiType.INTJ);
        Member member2 = new Member();
        member2.updateMbtiType(MbtiType.INTP);
        for (int i = 0; i < 20; i+=2) {
            boardRepository.save(
                    Board.createBoard("테스트" + i, "테스트" + i, "/test" + i, member1));
            boardRepository.save(
                    Board.createBoard("테스트" + i+1, "테스트" + i+1, "/test" + i+1, member2));
        }

        em.flush();
        em.clear();

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Board> boardList = boardRepository.findAllByPage(pageRequest);

        for (Board board : boardList) {
            board.getMember().getMbtiType();
        }

    }
}