package com.commbti.domain.bulletinboard.repository.impl;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
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
public class BulletinBoardRepositoryImpl implements BulletinBoardRepository {

    private final EntityManager em;

    @Override
    public void save(Bulletin bulletin) {
        em.persist(bulletin);
    }

    @Override
    public Optional<Bulletin> findById(Long id) {
        EntityGraph<Bulletin> entityGraph = em.createEntityGraph(Bulletin.class);
        entityGraph.addAttributeNodes("member");

        Map hints = new HashMap();
        hints.put("javax.persistence.fetchgraph", entityGraph);

        return Optional.ofNullable(em.find(Bulletin.class, id, hints));
    }

    @Override
    public List<Bulletin> findAllByPage(Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        int offset = page * size;

        String jpql = "select b from Bulletin b inner join fetch b.member m order by b.createdAt desc";

        return em.createQuery(jpql, Bulletin.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Bulletin> findPageByMbti(Pageable pageable, MbtiType mbtiType) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        int offset = page * size;

        String jpql = "select b from Bulletin b inner join fetch b.member m where m.mbtiType= :mbtiType order by b.createdAt desc";

        return em.createQuery(jpql, Bulletin.class)
                .setParameter("mbtiType", mbtiType)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public void delete(Bulletin bulletin) {
        em.remove(bulletin);
    }
}
