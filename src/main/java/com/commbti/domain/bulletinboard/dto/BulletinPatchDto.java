package com.commbti.domain.bulletinboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Setter @Getter
@NoArgsConstructor
public class BulletinPatchDto {
    @NotBlank(message = "제목은 필수값입니다.")
    @Size(max = 60, message = "제목은 1 ~ 60자 입력 가능합니다.")
    private String title;
    @NotBlank(message = "본문은 필수값입니다.")
    @Size(max = 1000, message = "본문은 1 ~ 1000자 입력 가능합니다.")
    private String content;
    private List<MultipartFile> files;
    private List<Long> deleteFiles;
}
