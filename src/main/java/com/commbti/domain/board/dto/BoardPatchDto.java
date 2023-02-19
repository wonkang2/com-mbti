package com.commbti.domain.board.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class BoardPatchDto {
    private String title;
    private String content;
    private MultipartFile file;

    private BoardPatchDto() {
    }

    public BoardPatchDto(String title, String content, MultipartFile file) {
        this.title = title;
        this.content = content;
        this.file = file;
    }
}
