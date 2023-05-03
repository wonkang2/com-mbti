package com.commbti.domain.bulletinboard.dto;

import com.commbti.domain.file.dto.ImageFileResponseDto;
import com.commbti.domain.file.entity.ImageFile;
import com.commbti.domain.member.entity.MbtiType;
import com.commbti.global.date.DateUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BulletinResponseDto {
    private Long memberId;
    private Long bulletinId;
    private MbtiType mbtiType;
    private String title;
    private String content;
    private List<ImageFileResponseDto> imageFileResponseDtoList;
    private Long viewCount;
    private String createdAt;

    @Builder
    public BulletinResponseDto(Long memberId, Long bulletinId, MbtiType mbtiType, String title, String content, List<ImageFile> imageFileList, Long viewCount, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.bulletinId = bulletinId;
        this.mbtiType = mbtiType;
        this.title = title;
        this.content = content;
        imageFileListToFilePathList(imageFileList);
        this.viewCount = viewCount;
        this.mbtiType = mbtiType;
        this.createdAt = DateUtils.convertToTimesAgo(createdAt);
    }

    private void imageFileListToFilePathList(List<ImageFile> imageFileList) {
        if (!imageFileList.isEmpty()) {
            this.imageFileResponseDtoList = imageFileList.stream().map(imageFile -> imageFile.toResponseDto()).collect(Collectors.toList());
        } else {
            imageFileResponseDtoList = null;
        }
    }
}
