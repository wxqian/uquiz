package com.leaf.uquiz.file.service;

import com.leaf.uquiz.file.domain.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FileManager {

    Page<File> findFiles(Integer spaceId, Long userId, String owner, Pageable pr);

    File saveFile(File file);
}
