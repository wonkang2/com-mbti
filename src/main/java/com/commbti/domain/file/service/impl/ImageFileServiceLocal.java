package com.commbti.domain.file.service.impl;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.file.entity.ImageFile;
import com.commbti.domain.file.repository.ImageFileRepository;
import com.commbti.domain.file.service.ImageFileService;
import com.commbti.global.exception.ApiException;
import com.commbti.global.exception.ExceptionCode;
import com.commbti.global.validation.ImageTypeValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ImageFileServiceLocal implements ImageFileService {

    private final ImageFileRepository imageFileRepository;
    @Value("${myapp.image.localUploadPath}")
    private String uploadPath;

    @Override
    public void uploadFiles(Bulletin bulletin, List<MultipartFile> uploadFiles) {
        if (uploadFiles == null) {
            return;
        }
        for (MultipartFile multipartFile : uploadFiles) {
            boolean isValidated = ImageTypeValidation.validFileType(multipartFile);
            if (!isValidated) {
                throw new ApiException(ExceptionCode.UNSUPPORTED_EXTENSIONS);
            }

            String originalFilename =
                    Normalizer.normalize(UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename(), Normalizer.Form.NFC);

            String saveFullPath = uploadPath + File.separator + originalFilename;

            Path savePath = Paths.get(saveFullPath);

            try {
                multipartFile.transferTo(savePath);
            } catch (IOException e) {
                log.info("IOException 발생: {}", e.getMessage());
                throw new ApiException(ExceptionCode.FILE_UPLOAD_FAILED);
            }

            ImageFile imageFile = ImageFile.createImageFile(saveFullPath, bulletin);
            imageFileRepository.save(imageFile);
        }
    }

    @Override
    public void updateFiles(Bulletin bulletin, List<Long> deleteImageIdList, List<MultipartFile> multipartFiles) {
        // TODO: 좀 더 최적화할 수 있는 방법 생각하기 -> 삭제될 파일을 업로드될 파일로 바꾼다던가
        deleteFiles(deleteImageIdList);
        uploadFiles(bulletin, multipartFiles);
    }

    @Override
    public void deleteFiles(List<Long> imageIdList) {
        if (imageIdList == null) {
            return;
        }
        for (Long imageId : imageIdList) {
            ImageFile imageFile = imageFileRepository.findById(imageId).orElseThrow(() -> new ApiException(ExceptionCode.IMAGE_NOT_FOUND));
            deleteLocalFile(imageFile.getFilepath());
            imageFileRepository.delete(imageFile);
        }
    }

    private void deleteLocalFile(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            // TODO:
            log.info("deleteLocalFile(): {} - 존재하지 않는 파일입니다.", filePath);
            return;
        }
        file.delete();
    }
}
