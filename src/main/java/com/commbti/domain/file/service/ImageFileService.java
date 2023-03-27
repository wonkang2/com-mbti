package com.commbti.domain.file.service;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageFileService {

    void uploadFiles(Bulletin bulletin, List<MultipartFile> multipartFile);

//    String updateFile(String filePath, MultipartFile multipartFile);
//
//    void deleteFile(String filePath);


}
