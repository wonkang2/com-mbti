package com.commbti.domain.member.entity;


import com.commbti.global.base.DateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Member extends DateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String username;
    private String password;
    @Enumerated(value = EnumType.STRING)
    private MbtiType mbtiType;

    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    private Member(String email, String username, String password, MbtiType mbtiType, MemberRole role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.mbtiType = mbtiType;
        this.role = role;
    }

    public static Member createMember(String email, String username, String password, PasswordEncoder passwordEncoder, MbtiType mbtiType) {
        return new Member(email, username, passwordEncoder.encode(password), mbtiType,MemberRole.USER);
    }
    public void updateMbtiType(MbtiType mbtiType) {
        this.mbtiType = mbtiType;
    }
}
