package com.commbti.domain.bulletinboard.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class BulletinPatchDto {
    private String title;
    private String content;
    private MultipartFile file;

    private BulletinPatchDto() {
    }

    public BulletinPatchDto(String title, String content, MultipartFile file) {
        this.title = title;
        this.content = content;
        this.file = file;
    }
}
