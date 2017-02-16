package com.leaf.uquiz.core.config;

import com.leaf.uquiz.file.FileConstants;
import com.leaf.uquiz.file.config.FileSettings;
import com.leaf.uquiz.file.domain.Zoom;
import com.leaf.uquiz.file.service.FileRepository;
import com.leaf.uquiz.file.service.Storage;
import com.leaf.uquiz.file.service.impl.FileServiceImpl;
import com.leaf.uquiz.file.service.impl.FileSystemStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/1
 */
@Configuration
public class FileConfig {
    @Autowired
    private FileSettings fileSettings;

    @Autowired
    private FileRepository fileRepository;

    @Bean(name = "mainStorage")
    public Storage mainStorage() {
        FileSystemStorage storage = new FileSystemStorage();
        storage.setDirectory(new File(this.fileSettings.getMainPath()));
        storage.setUseXsendfile(useXsendfile());
        storage.setId("main");
        return storage;
    }

    @Bean(name = "thumbStorage")
    public Storage thumbStorage() {
        FileSystemStorage storage = new FileSystemStorage();
        storage.setDirectory(new File(this.fileSettings.getThumbPath()));
        storage.setUseXsendfile(useXsendfile());
        storage.setId("thumb");
        return storage;
    }

    private boolean useXsendfile() {
        return this.fileSettings.isUseXsendfile();
    }

    @Bean
    public FileServiceImpl fileService() {
        FileServiceImpl fsi = new FileServiceImpl();
        fsi.setFileRepository(fileRepository);
        fsi.setStorage(mainStorage());

        fsi.addImageSpace(FileConstants.SPACE_UQUIZ_IMAGE, new Zoom(1080, 0), new Zoom(320, 0));
        fsi.addVideoSpace(FileConstants.SPACE_UQUIZ_VIDEO, "mp3");
        return fsi;
    }
}
