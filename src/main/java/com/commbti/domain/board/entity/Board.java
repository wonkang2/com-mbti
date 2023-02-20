package com.commbti.domain.board.entity;

import com.commbti.domain.board.dto.BoardListResponseDto;
import com.commbti.domain.member.entity.MbtiType;
import com.commbti.domain.member.entity.Member;
import com.commbti.global.base.DateTime;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Enumerated(value = EnumType.STRING)
    private MbtiType mbtiType;

    protected Board() {
    }

    private Board(String title, String content, String filePath, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.mbtiType = member.getMbtiType();
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

    public BoardListResponseDto toListResponseDto() {
        return BoardListResponseDto.builder()
                .id(this.id)
                .title(this.title)
                .username(this.member.getNickname())
                .mbtiType(this.member.getMbtiType())
                .createdAt(super.getCreatedAt()).build();
    }
}
