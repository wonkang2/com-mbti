package com.commbti.domain.member.entity;


import com.commbti.global.base.DateTime;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Member extends DateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String userId;
    private String password;
    @Enumerated(value = EnumType.STRING)
    private MbtiType mbtiType;

    public void updateMbtiType(MbtiType mbtiType) {
        this.mbtiType = mbtiType;
    }
}
