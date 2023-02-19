 package com.commbti.domain.board.repository;

import com.commbti.domain.board.entity.Board;

import java.util.Optional;

 public interface BoardRepository {

    void save(Board board);

    Optional<Board> findById(Long boardId);

}
