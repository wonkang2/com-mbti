package com.commbti.domain.member.entity;


import com.commbti.domain.admin.dto.AdminMemberResponseDto;
import com.commbti.domain.member.dto.MemberPatchDto;
import com.commbti.domain.member.dto.MemberResponseDto;
import com.commbti.global.base.DateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Member extends DateTime implements UserDetails {
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
    private Integer loginFailCount = 0;
    private boolean isAccountNonLocked = true; // 계정 잠긴 여부
    private LocalDateTime lastLoginDate; // 마지막 로그인 일자
    private boolean isNonBlocked = true; // 관리자에 의한 차단여부

    private Member(String email, String username, String password, MbtiType mbtiType, MemberRole role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.mbtiType = mbtiType;
        this.role = role;
    }

    public static Member createMember(String email, String username, String password, PasswordEncoder passwordEncoder, MbtiType mbtiType) {
        return new Member(email, username, passwordEncoder.encode(password), mbtiType,MemberRole.ROLE_USER);
    }
    public void modifyInfo(String newPassword, PasswordEncoder passwordEncoder, MbtiType newMbtiType) {
        Optional.ofNullable(newPassword)
                .ifPresent(password -> this.password = passwordEncoder.encode(password));
        Optional.ofNullable(newMbtiType)
                .ifPresent(mbtiType -> this.mbtiType = mbtiType);
    }

    // 로그인 실패 횟수 증가(로그인 실패 시 호출)
    public void increaseLoginFailCount() {
        if (loginFailCount > 4) {
            isAccountNonLocked = false;
            return;
        }
        this.loginFailCount ++;
    }

    // 로그인 관련된 필드 업데이트(로그인 성공 시 호출)
    public void updateLoginInfo() {
        this.loginFailCount = 0;
        this.lastLoginDate = LocalDateTime.now();
    }

    // 차단 여부 업데이트
    public void updateBlockStatus() {
        if (isNonBlocked) {
            this.isNonBlocked = false;
        } else {
            this.isNonBlocked = true;
        }
    }


    // ############################ toDTO ############################
    public AdminMemberResponseDto toAdminResponseDto() {
        return AdminMemberResponseDto.builder()
                .id(id)
                .email(email)
                .username(username)
                .mbtiType(mbtiType)
                .role(role)
                .lastLoginDate(lastLoginDate)
                .isAccountNonLocked(isAccountNonLocked)
                .isNonBlocked(isNonBlocked)
                .build();
    }

    public MemberResponseDto toResponseDto() {
        return MemberResponseDto.builder()
                .memberId(id)
                .email(email)
                .username(username)
                .mbtiType(mbtiType)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auth = new ArrayList();
        auth.add(new SimpleGrantedAuthority(getRole().toString()));
        return auth;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
