package com.commbti.domain.file.entity;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.file.dto.ImageFileResponseDto;
import lombok.*;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ImageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filepath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulletin_id")
    private Bulletin bulletin;

    private void setBulletin(Bulletin bulletin) {
        if (this.bulletin != null) {
            this.bulletin.getImageFileList().remove(this);
        }
        this.bulletin = bulletin;

        bulletin.getImageFileList().add(this);
    }
    private ImageFile(String filepath, Bulletin bulletin) {
        this.filepath = filepath;
        setBulletin(bulletin);
    }
    public static ImageFile createImageFile(String filePath, Bulletin bulletin) {
        return new ImageFile(filePath, bulletin);
    }

    public ImageFileResponseDto toResponseDto() {
        return new ImageFileResponseDto(this.id, this.filepath);
    }
}
