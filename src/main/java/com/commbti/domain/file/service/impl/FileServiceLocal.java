package com.commbti.domain.file.service.impl;

import com.commbti.domain.file.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.UUID;

@Service
public class FileServiceLocal implements FileService {
    @Value("${myapp.image.path}")
    private String uploadPath;

    @Override
    public String upload(MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename =
                Normalizer.normalize(multipartFile.getOriginalFilename(), Normalizer.Form.NFC);

        String filename = UUID.randomUUID() + "." + originalFilename;
        String saveFilename = uploadPath + File.separator + filename;

        Path savePath = Paths.get(saveFilename);

        try {
            multipartFile.transferTo(savePath);
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리 해야 함.
        }
        return saveFilename;
    }

    @Override
    public String update(String filePath, MultipartFile multipartFile) {
        if (filePath != null) {
            delete(filePath);
        }
        return upload(multipartFile);
    }

    @Override
    public void delete(String filePath) {
        String savedFilePath = uploadPath + File.separator + filePath;
        File file = new File(savedFilePath);

        if (!file.exists()) {
            return;
        }
        file.delete();
    }
}
