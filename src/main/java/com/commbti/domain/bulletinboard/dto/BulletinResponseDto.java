package com.commbti.domain.bulletinboard.dto;

import com.commbti.domain.file.entity.ImageFile;
import com.commbti.domain.member.entity.MbtiType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BulletinResponseDto {
    private Long bulletinId;
    private MbtiType mbtiType;
    private String title;
    private String content;
    private List<String> filePathList;
    private Long viewCount;
    private LocalDateTime createdAt;

    private BulletinResponseDto() {
    }
    @Builder
    public BulletinResponseDto(Long bulletinId, MbtiType mbtiType, String title, String content, List<ImageFile> imageFileList, Long viewCount, LocalDateTime createdAt) {
        this.bulletinId = bulletinId;
        this.mbtiType = mbtiType;
        this.title = title;
        this.content = content;
        imageFileListToFilePathList(imageFileList);
        this.viewCount = viewCount;
        this.mbtiType = mbtiType;
        this.createdAt = createdAt;
    }

    private void imageFileListToFilePathList(List<ImageFile> imageFileList) {
        if (!imageFileList.isEmpty()) {
            this.filePathList = imageFileList.stream().map(imageFile -> imageFile.getFilepath()).collect(Collectors.toList());
        } else {
            filePathList = null;
        }
    }
}
