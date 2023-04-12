package com.commbti.domain.comment.repository;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.comment.entity.Comment;
import com.commbti.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT c FROM Comment c JOIN FETCH c.member WHERE c.bulletin.id = :bulletinId ORDER BY c.createdAt DESC",
            countQuery = "SELECT count(c) FROM Comment c WHERE c.bulletin.id = :bulletinId")
    Page<Comment> findPageWithMemberMbtiByBulletinId(@Param("bulletinId") Long bulletinId, Pageable pageable);

    void deleteAllByBulletin_Id(Long bulletinId);

    @Override
    @EntityGraph(attributePaths = {"member"})
    Page<Comment> findAll(Pageable pageable);
}
