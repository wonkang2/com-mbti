package com.commbti.domain.member.repository;

import com.commbti.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findById(Long memberId);

}
