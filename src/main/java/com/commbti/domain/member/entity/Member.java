package com.commbti.domain.member.entity;


import com.commbti.global.base.DateTime;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Member extends DateTime {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String password;
    private String nickname;
    @Enumerated(value = EnumType.STRING)
    private MbtiType mbtiType;
}
