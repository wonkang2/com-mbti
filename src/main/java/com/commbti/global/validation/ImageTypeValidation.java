package com.commbti.global.validation;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class ImageTypeValidation {
    private static final List<String> FILE_TYPE = List.of(
            "image/gif", "image/jpeg", "image/jpg", "image/png"
    );

    public static boolean validFileType(MultipartFile multipartFile) {
        try {
            String mimeType = new Tika().detect(multipartFile.getInputStream());
            return FILE_TYPE.stream().anyMatch(type -> type.equalsIgnoreCase(mimeType));
        } catch (IOException e) {
            return false;
        }
    }
}
