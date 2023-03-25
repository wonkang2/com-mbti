package com.commbti.domain.file.repository;

import com.commbti.domain.file.entity.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
}
