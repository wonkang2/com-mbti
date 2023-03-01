package com.commbti.domain.bulletinboard.repository.impl;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
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
class BulletinBoardRepositoryImplTest {

    @Autowired
    private EntityManager em;
    private BulletinBoardRepository bulletinBoardRepository;

    @BeforeEach
    public void init() {
        bulletinBoardRepository = new BulletinBoardRepositoryImpl(em);
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
        Bulletin bulletin1 = Bulletin.createArticle("테스트1", "테스트1", "/test1", member1);
        Bulletin bulletin2 = Bulletin.createArticle("테스트2", "테스트2", "/test2", member1);
        bulletinBoardRepository.save(bulletin1);
        bulletinBoardRepository.save(bulletin2);

        Optional<Bulletin> optionalBoard = bulletinBoardRepository.findById(bulletin1.getId());
        assertThat(optionalBoard).isNotEmpty();
        assertThat(optionalBoard.get()).isEqualTo(bulletin1);
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 조회했을 때 조회되지 않는지 확인")
    void 존재하지않는_게시글_조회_테스트() {
        Member member1 = new Member();
        Bulletin bulletin1 = Bulletin.createArticle("테스트1", "테스트1", "/test1", member1);
        Bulletin bulletin2 = Bulletin.createArticle("테스트2", "테스트2", "/test2", member1);
        bulletinBoardRepository.save(bulletin1);
        bulletinBoardRepository.save(bulletin2);


        Optional<Bulletin> optionalBoard = bulletinBoardRepository.findById(0L);


        assertThat(optionalBoard).isEmpty();
    }


    @Test
    @DisplayName("게시글 페이지가 제대로 조회되는지 확인")
    void 게시글_목록_조회_테스트() {
        Member member1 = new Member();
        Member member2 = new Member();
        Bulletin bulletin1 = Bulletin.createArticle("테스트1", "테스트1", "/test1", member1);
        bulletinBoardRepository.save(bulletin1);
        Bulletin bulletin2 = Bulletin.createArticle("테스트2", "테스트2", "/test2", member2);
        bulletinBoardRepository.save(bulletin2);
        Bulletin bulletin3 = Bulletin.createArticle("테스트3", "테스트3", "/test3", member1);
        bulletinBoardRepository.save(bulletin3);

        PageRequest pageRequest = PageRequest.of(0, 20);


        List<Bulletin> bulletinList = bulletinBoardRepository.findAllByPage(pageRequest);


        assertThat(bulletinList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("게시글 페이지가 존재하지 않을 떄 0개의 값이 나오는지 확인")
    void 게시글_목록_조회_테스트1() {
        Member member1 = new Member();
        Member member2 = new Member();
        Bulletin bulletin1 = Bulletin.createArticle("테스트1", "테스트1", "/test1", member1);
        bulletinBoardRepository.save(bulletin1);
        Bulletin bulletin2 = Bulletin.createArticle("테스트2", "테스트2", "/test2", member2);
        bulletinBoardRepository.save(bulletin2);
        Bulletin bulletin3 = Bulletin.createArticle("테스트3", "테스트3", "/test3", member1);
        bulletinBoardRepository.save(bulletin3);
        PageRequest pageRequest = PageRequest.of(1, 20);

        List<Bulletin> bulletinList = bulletinBoardRepository.findAllByPage(pageRequest);

        assertThat(bulletinList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 페이지가 최신순으로 조회되는지 확인")
    void 게시글_목록_정렬_테스트() {
        Member member1 = new Member();
        Member member2 = new Member();
        Bulletin bulletin1 = Bulletin.createArticle("테스트1", "테스트1", "/test1", member1);
        bulletinBoardRepository.save(bulletin1);
        Bulletin bulletin2 = Bulletin.createArticle("테스트2", "테스트2", "/test2", member2);
        bulletinBoardRepository.save(bulletin2);
        Bulletin bulletin3 = Bulletin.createArticle("테스트3", "테스트3", "/test3", member1);
        bulletinBoardRepository.save(bulletin3);

        PageRequest pageRequest = PageRequest.of(0, 20);


        List<Bulletin> bulletinList = bulletinBoardRepository.findAllByPage(pageRequest);


        assertThat(bulletinList.get(0).getCreatedAt()).isAfter(bulletinList.get(1).getCreatedAt());
        assertThat(bulletinList.get(1).getCreatedAt()).isAfter(bulletinList.get(2).getCreatedAt());
    }

    @Test
    @DisplayName("mbti별로 게시글이 제대로 조회되는지 확인")
    void mbti필터_게시글_목록_조회_테스트() {
        Member member1 = new Member();
        member1.updateMbtiType(MbtiType.INTJ);
        Member member2 = new Member();
        member2.updateMbtiType(MbtiType.INTP);
        Bulletin bulletin1 = Bulletin.createArticle("테스트1", "테스트1", "/test1", member1);
        bulletinBoardRepository.save(bulletin1);
        Bulletin bulletin2 = Bulletin.createArticle("테스트2", "테스트2", "/test2", member2);
        bulletinBoardRepository.save(bulletin2);
        Bulletin bulletin3 = Bulletin.createArticle("테스트3", "테스트3", "/test3", member1);
        bulletinBoardRepository.save(bulletin3);

        PageRequest pageRequest = PageRequest.of(0, 20);


        List<Bulletin> bulletinList = bulletinBoardRepository.findPageByMbti(pageRequest, member1.getMbtiType());


        assertThat(bulletinList).contains(bulletin1, bulletin3);
        assertThat(bulletinList).doesNotContain(bulletin2);
    }

    @Test
    @DisplayName("mbti별로 게시글이 최신순으로 조회되는지 확인")
    void mbti필터_게시글_목록_정렬_테스트() {
        Member member1 = new Member();
        member1.updateMbtiType(MbtiType.INTJ);
        Member member2 = new Member();
        member2.updateMbtiType(MbtiType.INTP);
        Bulletin bulletin1 = Bulletin.createArticle("테스트1", "테스트1", "/test1", member1);
        bulletinBoardRepository.save(bulletin1);
        Bulletin bulletin2 = Bulletin.createArticle("테스트2", "테스트2", "/test2", member2);
        bulletinBoardRepository.save(bulletin2);
        Bulletin bulletin3 = Bulletin.createArticle("테스트3", "테스트3", "/test3", member1);
        bulletinBoardRepository.save(bulletin3);

        PageRequest pageRequest = PageRequest.of(0, 20);


        List<Bulletin> bulletinList = bulletinBoardRepository.findPageByMbti(pageRequest, member1.getMbtiType());


        assertThat(bulletinList.get(0).getCreatedAt()).isAfter(bulletinList.get(1).getCreatedAt());
    }

    @Test
    @DisplayName("mbti별로 게시글이 페이징처리되서 조회되는지 확인1")
    void mbti필터_게시글_목록_페이지_조회_테스트1() {
        Member member1 = new Member();
        member1.updateMbtiType(MbtiType.INTJ);
        Member member2 = new Member();
        member2.updateMbtiType(MbtiType.INTP);
        for (int i = 0; i < 20; i+=2) {
            bulletinBoardRepository.save(
                    Bulletin.createArticle("테스트" + i, "테스트" + i, "/test" + i, member1));
            bulletinBoardRepository.save(
                    Bulletin.createArticle("테스트" + i+1, "테스트" + i+1, "/test" + i+1, member2));
        }

        PageRequest pageRequest = PageRequest.of(0, 10);


        List<Bulletin> bulletinList = bulletinBoardRepository.findPageByMbti(pageRequest, member1.getMbtiType());


        assertThat(bulletinList.size()).isEqualTo(10);
        assertThat(bulletinList.get(0).getCreatedAt()).isAfter(bulletinList.get(1).getCreatedAt());
        assertThat(bulletinList.get(0).getMember().getMbtiType()).isEqualTo(bulletinList.get(9).getMember().getMbtiType());
    }

    @Test
    @DisplayName("mbti별로 게시글이 페이징처리되서 조회되는지 확인2")
    void mbti필터_게시글_목록_페이지_조회_테스트2() {
        Member member1 = new Member();
        member1.updateMbtiType(MbtiType.INTJ);
        Member member2 = new Member();
        member2.updateMbtiType(MbtiType.INTP);
        for (int i = 0; i < 20; i+=2) {
            bulletinBoardRepository.save(
                    Bulletin.createArticle("테스트" + i, "테스트" + i, "/test" + i, member1));
            bulletinBoardRepository.save(
                    Bulletin.createArticle("테스트" + i+1, "테스트" + i+1, "/test" + i+1, member2));
        }

        PageRequest pageRequest = PageRequest.of(1, 10);


        List<Bulletin> bulletinList = bulletinBoardRepository.findPageByMbti(pageRequest, member1.getMbtiType());


        assertThat(bulletinList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("삭제 확인")
    void 삭제_테스트() {
        Member member1 = new Member();
        Bulletin bulletin1 = Bulletin.createArticle("테스트1", "테스트1", "/test1", member1);
        bulletinBoardRepository.save(bulletin1);

        bulletinBoardRepository.delete(bulletin1);

        assertThat(bulletinBoardRepository.findById(bulletin1.getId())).isEmpty();
    }

    @Test
    @DisplayName("N+1 문제 확인")
    void n_plus_1_test() {
        Member member1 = new Member();
        member1.updateMbtiType(MbtiType.INTJ);
        Member member2 = new Member();
        member2.updateMbtiType(MbtiType.INTP);
        for (int i = 0; i < 20; i+=2) {
            bulletinBoardRepository.save(
                    Bulletin.createArticle("테스트" + i, "테스트" + i, "/test" + i, member1));
            bulletinBoardRepository.save(
                    Bulletin.createArticle("테스트" + i+1, "테스트" + i+1, "/test" + i+1, member2));
        }

        em.flush();
        em.clear();

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Bulletin> bulletinList = bulletinBoardRepository.findAllByPage(pageRequest);

        for (Bulletin bulletin : bulletinList) {
            bulletin.getMember().getMbtiType();
        }

    }
}