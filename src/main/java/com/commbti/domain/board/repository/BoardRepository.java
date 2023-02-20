 package com.commbti.domain.board.repository;

import com.commbti.domain.board.entity.Board;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

 public interface BoardRepository {

    void save(Board board);

    Optional<Board> findById(Long boardId);

     List<Board> findAllByPage(Pageable page);

}
