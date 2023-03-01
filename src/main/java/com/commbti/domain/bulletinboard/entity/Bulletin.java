package com.commbti.domain.bulletinboard.entity;

import com.commbti.domain.bulletinboard.dto.BoardResponseDto;
import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.member.entity.Member;
import com.commbti.global.base.DateTime;
import lombok.Getter;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
public class Bulletin extends DateTime {

    @Id @GeneratedValue
    @Column(name = "article_id")
    private Long id;
    private String title;
    private String content;
    private String filePath;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Bulletin() {
    }

    private Bulletin(String title, String content, String filePath, Member member) {
        this.title = title;
        this.filePath = filePath;
        this.content = content;
        this.member = member;
    }

    public static Bulletin createArticle(String title, String content, String filePath, Member member) {
        return new Bulletin(title, content, filePath, member);
    }

    public void update(String optionalTitle, String optionalContent, String optionalFilePath) {
        Optional.ofNullable(optionalTitle)
                .ifPresent(title -> this.title = title);
        Optional.ofNullable(optionalContent)
                .ifPresent(content -> this.content = content);
        Optional.ofNullable(optionalFilePath)
                .ifPresent(filePath -> this.filePath = filePath);
    }

    /* --------------------------- toDto --------------------------- */
    public BoardResponseDto toBoardResponseDto() {
        return BoardResponseDto.builder()
                .id(this.id)
                .title(this.title)
                .username(this.member.getNickname())
                .mbtiType(this.member.getMbtiType())
                .createdAt(super.getCreatedAt()).build();
    }

    public BulletinResponseDto toBulletinResponseDto() {
        return BulletinResponseDto.builder()
                .content(this.content)
                .title(this.title)
                .filePath(this.filePath)
                .nickname(member.getNickname())
                .mbtiType(member.getMbtiType())
                .createdAt(super.getCreatedAt())
                .build();
    }
}
