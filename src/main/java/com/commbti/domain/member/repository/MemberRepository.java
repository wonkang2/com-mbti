package com.commbti.domain.member.repository;

import com.commbti.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
    Page<Member> findAll(Pageable pageable);
    Page<Member> findByEmailStartsWith(Pageable pageable, String email);
    Page<Member> findByUsernameStartsWith(Pageable pageable, String username);
    Page<Member> findById(Pageable pageable, Long id);
    Optional<Member> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
