package com.commbti.domain.board.repository.impl;

import com.commbti.domain.board.entity.Board;
import com.commbti.domain.board.repository.BoardRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
}
