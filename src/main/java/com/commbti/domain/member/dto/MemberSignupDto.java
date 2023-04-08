package com.commbti.domain.member.dto;

import com.commbti.domain.member.entity.MbtiType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberSignupDto {

    private String username;
    private String password;
    private String email;
    private MbtiType mbti;
}
