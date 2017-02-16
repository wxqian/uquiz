package com.leaf.uquiz.file.service;


import com.leaf.uquiz.file.domain.File;
import com.leaf.uquiz.file.domain.Space;

import java.io.InputStream;

public interface FileService {

    File getFile(long id);

//    Map<Long, File> mgetFile(Collection<Long> ids);

    File saveFile(File file);

    File saveInputStreamFile(File file, InputStream inputStream);

    File saveDiskFile(File file, java.io.File diskFile);

    void saveUrlFile(long id, String url);

    VFile getVFile(File file);
    
    Space getSpace(String spaceKey);
	
	Space getSpace(int spaceId);
}
