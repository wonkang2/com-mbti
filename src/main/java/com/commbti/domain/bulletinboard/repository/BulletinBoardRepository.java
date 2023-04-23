package com.commbti.domain.bulletinboard.repository;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface BulletinBoardRepository extends JpaRepository<Bulletin, Long> {

    @Query(value = "SELECT b FROM Bulletin b JOIN FETCH b.member ORDER BY b.createdAt DESC",
            countQuery = "SELECT count(b) FROM Bulletin b")
    Page<Bulletin> findPageWithMemberMbti(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"member"})
    Page<Bulletin> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"member"})
    @Override
    Optional<Bulletin> findById(Long bulletinId);

    @EntityGraph(attributePaths = {"member"})
    Page<Bulletin> findById(Pageable pageable, Long id);
    @EntityGraph(attributePaths = {"member"})
    Page<Bulletin> findByTitleContains(Pageable pageable, String title);
    @EntityGraph(attributePaths = {"member"})
    Page<Bulletin> findByContentContains(Pageable pageable, String content);
    @EntityGraph(attributePaths = {"member"})
    Page<Bulletin> findByMember_usernameContains(Pageable pageable, String username);
    @EntityGraph(attributePaths = {"member"})
    Page<Bulletin> findByMember_id(Pageable pageable, Long id);

}
