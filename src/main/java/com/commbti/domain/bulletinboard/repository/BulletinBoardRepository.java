package com.commbti.domain.bulletinboard.repository;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BulletinBoardRepository extends JpaRepository<Bulletin, Long> {

    @Query(value = "SELECT b FROM Bulletin b JOIN FETCH b.member ORDER BY b.createdAt DESC",
            countQuery = "SELECT count(b) FROM Bulletin b")
    Page<Bulletin> findPageWithMemberMbti(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"member"})
    Page<Bulletin> findAll(Pageable pageable);
}
