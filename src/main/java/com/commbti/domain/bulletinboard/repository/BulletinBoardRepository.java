package com.commbti.domain.bulletinboard.repository;

import com.commbti.domain.bulletinboard.dto.BoardResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface BulletinBoardRepository extends JpaRepository<Bulletin, Long> {
    @Query(value = "SELECT DISTINCT new com.commbti.domain.bulletinboard.dto.BoardResponseDto(b.id, b.title, b.content, m.mbtiType, b.createdAt, b.viewCount, b.commentCount, f.filepath)" +
            " FROM Bulletin b" +
            " JOIN b.member m" +
            " JOIN b.imageFileList f" +
            " ORDER BY b.createdAt DESC",
            countQuery = "SELECT count(b) FROM Bulletin b")
    Page<BoardResponseDto> findBoard(Pageable pageable);

    @Query(value = "SELECT DISTINCT new com.commbti.domain.bulletinboard.dto.BoardResponseDto(b.id, b.title, b.content, m.mbtiType, b.createdAt, b.viewCount, b.commentCount, f.filepath)" +
            " FROM Bulletin b" +
            " JOIN b.member m" +
            " JOIN b.imageFileList f" +
            " WHERE b.title LIKE %:title%" +
            " ORDER BY b.createdAt DESC",
            countQuery = "SELECT count(b) FROM Bulletin b")
    Page<BoardResponseDto> findBoardByTitleContains(Pageable pageable, @Param("title") String title);

    @Query(value = "SELECT DISTINCT new com.commbti.domain.bulletinboard.dto.BoardResponseDto(b.id, b.title, b.content, m.mbtiType, b.createdAt, b.viewCount, b.commentCount, f.filepath)" +
            " FROM Bulletin b" +
            " JOIN b.member m" +
            " JOIN b.imageFileList f" +
            " WHERE b.content LIKE %:content%" +
            " ORDER BY b.createdAt DESC",
            countQuery = "SELECT count(b) FROM Bulletin b")
    Page<BoardResponseDto> findBoardByContentContains(Pageable pageable, @Param("content") String content);


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
