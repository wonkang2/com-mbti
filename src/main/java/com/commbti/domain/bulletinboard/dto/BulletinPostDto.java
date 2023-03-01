package com.commbti.domain.bulletinboard.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class BulletinPostDto {
    private String title;
    private String content;
    private MultipartFile file;

    private BulletinPostDto() {
    }

    public BulletinPostDto(String title, String content, MultipartFile file) {
        this.title = title;
        this.content = content;
        this.file = file;
    }
}
