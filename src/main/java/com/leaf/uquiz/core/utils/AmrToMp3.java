package com.leaf.uquiz.core.utils;

import com.leaf.uquiz.file.FileConstants;
import com.leaf.uquiz.file.convert.FileConverter;
import com.leaf.uquiz.file.domain.Space;
import com.leaf.uquiz.file.dto.FileDto;
import com.leaf.uquiz.file.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/3/8
 */
@Component
public class AmrToMp3 {

    @Autowired
    private static FileService fileService;

    @Autowired
    private static FileConverter fileConverter;

    private static Logger logger = LoggerFactory.getLogger(AmrToMp3.class);

    public static boolean convert(String amrFile, String mp3File) {
        Process process = null;
        try {
            File mp3 = new File(mp3File);
            if (mp3.exists()) {
                return true;
            }
            process = Runtime.getRuntime().exec("ffmpeg -i " + amrFile + " " + mp3File);
            process.waitFor();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (process != null) {
                process.destroy();
            }
        }

        return false;
    }

    public static FileDto convert2(String amrFile, String mp3File) {
        Process process = null;
        try {
            File mp3 = new File(mp3File);
            if (mp3.exists()) {
                mp3.delete();
            }
            process = Runtime.getRuntime().exec("ffmpeg -i " + amrFile + " " + mp3File);
            process.waitFor();
            Space s = fileService.getSpace(FileConstants.SPACE_UQUIZ_VIDEO);
            com.leaf.uquiz.file.domain.File file = new com.leaf.uquiz.file.domain.File();
            file.setSpaceId(s.getId());
            file.setName("111111");
            file.setOwner("-1");
            file.setName("-1");
            file.setUserId(0L);
            return fileConverter.convert(fileService.saveInputStreamFile(file, process.getInputStream()));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return null;
    }
}
