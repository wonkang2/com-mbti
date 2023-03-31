package com.commbti.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageFileResponseDto {

    private Long imageId;
    private String imagePath;
}
