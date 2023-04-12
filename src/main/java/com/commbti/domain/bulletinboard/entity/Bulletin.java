package com.commbti.domain.bulletinboard.entity;

import com.commbti.domain.admin.dto.AdminBulletinResponseDto;
import com.commbti.domain.bulletinboard.dto.BoardResponseDto;
import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.file.entity.ImageFile;
import com.commbti.domain.member.entity.Member;
import com.commbti.global.base.DateTime;
import lombok.Getter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
public class Bulletin extends DateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulletin_id")
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String thumbnailPath;
    private Long viewCount;

    @Formula("(select count(1) from Comment c where c.bulletin_id = bulletin_id)")
    private Long commentCount;

    @OneToMany(mappedBy = "bulletin", cascade = CascadeType.ALL)
    private List<ImageFile> imageFileList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Bulletin() {
    }

    private Bulletin(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.viewCount = 0L;
    }

    public static Bulletin createArticle(String title, String content, Member member) {
        return new Bulletin(title, content, member);
    }

    public void update(String optionalTitle, String optionalContent) {
        Optional.ofNullable(optionalTitle)
                .ifPresent(title -> this.title = title);
        Optional.ofNullable(optionalContent)
                .ifPresent(content -> this.content = content);
    }

    public void updateThumbnail(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public void addViewCount() {
        this.viewCount++;
    }


    /* --------------------------- toDto --------------------------- */
    public BulletinResponseDto toBulletinResponseDto() {
        return BulletinResponseDto.builder()
                .memberId(this.member.getId())
                .bulletinId(this.id)
                .mbtiType(member.getMbtiType())
                .title(this.title)
                .content(this.content)
                .imageFileList(this.imageFileList)
                .viewCount(this.viewCount)
                .createdAt(super.getCreatedAt())
                .build();
    }

    public BoardResponseDto toBoardResponseDto() {
        return BoardResponseDto.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .mbtiType(this.member.getMbtiType())
                .createdAt(this.getCreatedAt())
                .viewCount(this.viewCount)
                .commentCount(this.commentCount)
                .thumbnailPath(this.thumbnailPath)
                .build();
    }

    public AdminBulletinResponseDto toAdminResponseDto() {
        return AdminBulletinResponseDto.builder()
                .id(id)
                .memberId(member.getId())
                .username(member.getUsername())
                .title(title)
                .content(content)
                .createdAt(getCreatedAt())
                .lastModifiedAt(getModifiedAt())
                .viewCount(viewCount)
                .commentCount(commentCount).build();
    }
}
