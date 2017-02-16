package com.leaf.uquiz.file.service.impl;

import com.leaf.uquiz.file.domain.File;
import com.leaf.uquiz.file.service.FileManager;
import com.leaf.uquiz.file.service.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public class FileManagerImpl implements FileManager {

    @Autowired
    @Qualifier("fileRepository")
    private FileRepository fileRepository;

    @Override
    public Page<File> findFiles(Integer spaceId, Long userId, String owner, Pageable pr) {
//        Params params = Params.param();
//        if (spaceId != null) {
//            params.put("spaceId", spaceId);
//        }
//        if (userId != null) {
//            params.put("userId", userId);
//        }
//        if (StringUtils.isNotBlank(owner)) {
//            params.put("owner", owner);
//        }
//        if (params.size() == 0) {
//            return fileRepository.find("$findFiles").fetch(pr);
//        } else {
//            return fileRepository.find("$findFiles", params).fetch(pr);
//        }
        return null;
    }

    @Override
    @Transactional
    public File saveFile(File file) {
        return fileRepository.save(file);
    }

}
