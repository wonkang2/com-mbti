 package com.commbti.domain.bulletinboard.repository;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.member.entity.MbtiType;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

 public interface BulletinBoardRepository {

    void save(Bulletin bulletin);

    Optional<Bulletin> findById(Long id);

     List<Bulletin> findAllByPage(Pageable page);

     List<Bulletin> findPageByMbti(Pageable pageable, MbtiType mbtiType);

     void delete(Bulletin bulletin);

}
