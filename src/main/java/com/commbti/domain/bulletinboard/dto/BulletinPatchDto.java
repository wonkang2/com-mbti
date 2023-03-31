package com.commbti.domain.bulletinboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter @Getter
@NoArgsConstructor
public class BulletinPatchDto {
    private String title;
    private String content;
    private List<MultipartFile> files;
    private List<Long> deleteFiles;
}
