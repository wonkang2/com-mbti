package com.commbti.domain.board.entity;

import com.commbti.domain.board.dto.BoardListResponseDto;
import com.commbti.domain.board.dto.BoardResponseDto;
import com.commbti.domain.member.entity.Member;
import com.commbti.global.base.DateTime;
import lombok.Getter;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
public class Board extends DateTime {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;
    private String title;
    private String content;
    private String filePath;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Board() {
    }

    private Board(String title, String content, String filePath, Member member) {
        this.title = title;
        this.filePath = filePath;
        this.content = content;
        this.member = member;
    }

    public static Board createBoard(String title, String content, String filePath, Member member) {
        return new Board(title, content, filePath, member);
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
    public BoardListResponseDto toListResponseDto() {
        return BoardListResponseDto.builder()
                .id(this.id)
                .title(this.title)
                .username(this.member.getNickname())
                .mbtiType(this.member.getMbtiType())
                .createdAt(super.getCreatedAt()).build();
    }

    public BoardResponseDto toResponseDto() {
        return BoardResponseDto.builder()
                .content(this.content)
                .title(this.title)
                .filePath(this.filePath)
                .nickname(member.getNickname())
                .mbtiType(member.getMbtiType())
                .createdAt(super.getCreatedAt())
                .build();
    }
}
