package com.commbti.domain.comment.repository;

import com.commbti.domain.comment.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    void save(Comment comment);

    Optional<Comment> findById(Long commentId);

    List<Comment> findAllByBulletinId(Long bulletinId);

    void delete(Comment comment);
}
