package com.commbti.domain.board.repository.impl;

import com.commbti.domain.board.entity.Board;
import com.commbti.domain.board.repository.BoardRepository;
import com.commbti.domain.member.entity.MbtiType;
import com.commbti.domain.member.entity.Member;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BoardRepositoryImpl implements BoardRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Board board) {
        em.persist(board);
    }

    @Override
    @EntityGraph(attributePaths = {"member"})
    public Optional<Board> findById(Long boardId) {
        return Optional.ofNullable(em.find(Board.class, boardId));
    }

    @Override
    public List<Board> findAllByPage(Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        int offset = page * size;
        return em.createQuery("select b from Board b inner join fetch b.member m order by b.createdAt desc", Board.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Board> findPageByMbti(Pageable pageable, MbtiType mbtiType) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        int offset = page * size;

        String jpql = "select b from Board b join b.member m where m.mbtiType= :mbtiType";

        return em.createQuery(jpql, Board.class)
                .setParameter("mbtiType", mbtiType)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public void delete(Board board) {
        em.remove(board);
    }
}
