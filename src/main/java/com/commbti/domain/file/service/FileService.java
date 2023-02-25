package com.commbti.domain.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FileService {

    String upload(MultipartFile multipartFile);

    String update(String filePath, MultipartFile multipartFile);

    void delete(String filePath);


}
