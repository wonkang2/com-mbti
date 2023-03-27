package com.commbti.domain.file.entity;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import lombok.*;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ImageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filename;
    private String filepath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulletin_id")
    private Bulletin bulletin;

    private void setBulletin(Bulletin bulletin) {
        if (this.bulletin != null) {
            this.bulletin.getImageFileList().remove(this);
        }
        this.bulletin = bulletin;

        if (bulletin.getThumbnailPath() == null) {
            bulletin.updateThumbnail(this.filepath);
        }
        bulletin.getImageFileList().add(this);
    }
    private ImageFile(String filename, String filepath, Bulletin bulletin) {
        this.filename = filename;
        this.filepath = filepath;
        setBulletin(bulletin);
    }
    public static ImageFile createImageFile(String fileName, String filePath, Bulletin bulletin) {
        return new ImageFile(fileName, filePath, bulletin);
    }
}
