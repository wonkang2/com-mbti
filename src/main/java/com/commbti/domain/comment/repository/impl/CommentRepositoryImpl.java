package com.commbti.domain.comment.repository.impl;

import com.commbti.domain.comment.entity.Comment;
import com.commbti.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final EntityManager em;

    @Override
    public void save(Comment comment) {
        if (comment.getId() == null) {
            return;
        }
        em.persist(comment);
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        Comment comment = em.find(Comment.class, commentId);

        return Optional.ofNullable(comment);
    }

    @Override
    public List<Comment> findAllByBulletinId(Long bulletinId) {

        String jpql = "select c from Comment c inner join fetch c.member m where c.bulletin.id= :bulletinId order by c.createdAt desc";
        return em.createQuery(jpql, Comment.class)
                .setParameter("bulletinId", bulletinId)
                .getResultList();
    }

    @Override
    public void delete(Comment comment) {
        em.remove(comment);
    }
}
