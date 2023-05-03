package com.commbti.domain.bulletinboard.entity;

import com.commbti.domain.admin.dto.AdminBulletinResponseDto;
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
    @Column(name = "title", nullable = false, length = 60)
    private String title;
    @Column(name = "content", nullable = false, length = 1000)
    private String content;
    private Long viewCount = 0L;

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
