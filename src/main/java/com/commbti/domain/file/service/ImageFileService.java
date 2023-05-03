package com.commbti.domain.file.service;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageFileService {

    void uploadFiles(Bulletin bulletin, List<MultipartFile> multipartFiles);

    void updateFiles(Bulletin bulletin, List<Long> deleteImageIdList, List<MultipartFile> multipartFiles);
    void deleteFiles(List<Long> imageIdList);


}
