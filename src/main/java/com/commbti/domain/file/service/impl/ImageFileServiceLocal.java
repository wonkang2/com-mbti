package com.commbti.domain.file.service.impl;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.file.entity.ImageFile;
import com.commbti.domain.file.repository.ImageFileRepository;
import com.commbti.domain.file.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImageFileServiceLocal implements ImageFileService {

    private final ImageFileRepository imageFileRepository;
    @Value("${myapp.image.localUploadPath}")
    private String uploadPath;

    @Override
    public void uploadFiles(Bulletin bulletin, List<MultipartFile> uploadFiles) {
        if (uploadFiles.isEmpty()) {
            return;
        }
        List<MultipartFile>  multipartFileList = uploadFiles.stream().filter(multipartFile -> !multipartFile.isEmpty()).collect(Collectors.toList());
        for (MultipartFile multipartFile : multipartFileList) {

            String originalFilename =
                    Normalizer.normalize(UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename(), Normalizer.Form.NFC);

            String saveFullPath = uploadPath + File.separator + originalFilename;

            Path savePath = Paths.get(saveFullPath);

            try {
                multipartFile.transferTo(savePath);
            } catch (IOException e) {
                e.printStackTrace(); // 예외 처리 해야 함.
            }

            ImageFile imageFile = ImageFile.createImageFile(originalFilename, saveFullPath, bulletin);
            imageFileRepository.save(imageFile);
        }
    }

//    @Override
//    public String update(String filePath, MultipartFile multipartFile) {
//        if (filePath != null) {
//            delete(filePath);
//        }
//        return upload(multipartFile);
//    }
//
//    @Override
//    public void delete(String filePath) {
//        String savedFilePath = uploadPath + File.separator + filePath;
//        File file = new File(savedFilePath);
//
//        if (!file.exists()) {
//            return;
//        }
//        file.delete();
//    }
}
