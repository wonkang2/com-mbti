package com.commbti.domain.comment.entity;

import com.commbti.domain.admin.dto.AdminCommentResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.comment.dto.CommentResponseDto;
import com.commbti.domain.member.entity.Member;
import com.commbti.global.base.DateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends DateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @Column(nullable = false, length = 255)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulletin_id")
    private Bulletin bulletin;

    private Comment(Member member, Bulletin bulletin, String content) {
        this.member = member;
        this.bulletin = bulletin;
        this.content = content;
    }

    public static Comment create(Member member, Bulletin bulletin, String content) {
        return new Comment(member, bulletin, content);
    }

    public void update(String content) {
        this.content = content;
    }

    public CommentResponseDto toResponseDto() {
        return new CommentResponseDto(this.getMember().getId(),this.id, this.member.getMbtiType(), this.content, this.getCreatedAt());
    }

    public AdminCommentResponseDto toAdminResponseDto() {
        return AdminCommentResponseDto.builder()
                .id(id)
                .memberId(member.getId())
                .username(getMember().getUsername())
                .content(content)
                .createdAt(getCreatedAt())
                .lastModifiedAt(getModifiedAt())
                .build();
    }
}
