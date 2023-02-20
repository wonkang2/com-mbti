package com.commbti.domain.board.repository.impl;

import com.commbti.domain.board.entity.Board;
import com.commbti.domain.board.repository.BoardRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

}
