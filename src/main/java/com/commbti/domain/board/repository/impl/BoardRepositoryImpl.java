package com.commbti.domain.board.repository.impl;

import com.commbti.domain.board.entity.Board;
import com.commbti.domain.board.repository.BoardRepository;
import com.commbti.domain.member.entity.MbtiType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {

    private final EntityManager em;

    @Override
    public void save(Board board) {
        em.persist(board);
    }

    @Override
    public Optional<Board> findById(Long boardId) {
        EntityGraph<Board> entityGraph = em.createEntityGraph(Board.class);
        entityGraph.addAttributeNodes("member");

        Map hints = new HashMap();
        hints.put("javax.persistence.fetchgraph", entityGraph);

        return Optional.ofNullable(em.find(Board.class, boardId, hints));
    }

    @Override
    public List<Board> findAllByPage(Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        int offset = page * size;

        String jpql = "select b from Board b inner join fetch b.member m order by b.createdAt desc";

        return em.createQuery(jpql, Board.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Board> findPageByMbti(Pageable pageable, MbtiType mbtiType) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        int offset = page * size;

        String jpql = "select b from Board b inner join fetch b.member m where m.mbtiType= :mbtiType order by b.createdAt desc";

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
