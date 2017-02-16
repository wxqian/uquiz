package com.leaf.uquiz.file.service.impl;

import com.leaf.uquiz.file.domain.Dimension;
import com.leaf.uquiz.file.domain.Space;
import com.leaf.uquiz.file.domain.SpaceType;
import com.leaf.uquiz.file.domain.Zoom;
import com.leaf.uquiz.file.service.FileService;
import com.leaf.uquiz.file.service.Storage;
import com.leaf.uquiz.file.service.VFile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * The Class FileUtils.
 *
 * @author <a href="mailto:stormning@163.com">ningzhou</a>
 * @since 2014-10-10
 */
@Component
public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private static FileService fileService;

    private static Storage thumbStorage;

    @Autowired
    public void init(FileService fileService, @Qualifier("thumbStorage") Storage thumbStorage) {
        FileUtils.fileService = fileService;
        FileUtils.thumbStorage = thumbStorage;
    }

    /**
     * 得到amr的时长(毫秒)
     */
    public static long getAmrDuration(File file) {
        long duration = 0;
        int[] packedSize = {12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0};
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            long length = file.length();// 文件的长度
            int pos = 6;// 设置初始位置
            int frameCount = 0;// 初始帧数
            int packedPos = -1;
            // ///////////////////////////////////////////////////
            byte[] datas = new byte[1];// 初始数据值
            while (pos <= length) {
                randomAccessFile.seek(pos);
                if (randomAccessFile.read(datas, 0, 1) != 1) {
                    duration = length > 0 ? ((length - 6) / 650) : 0;
                    break;
                }
                packedPos = (datas[0] >> 3) & 0x0F;
                pos += packedSize[packedPos] + 1;
                frameCount++;
            }
            duration += frameCount * 20;// 帧数*20
        } catch (Exception e) {
            LOGGER.error("Get amr duration error", e);
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    // ignore
                    LOGGER.error("Close random access file error", e);
                }
            }
        }
        return duration;
    }

    public static Dimension getZoomImageSize(int imgWidth, int imgHeight, int maxWidth, int maxHeight, int cropHeight) {
        Double ratio = null;
        if (maxWidth > 0 && imgWidth > maxWidth) {
            ratio = (double) maxWidth / imgWidth;
        }
        if (maxHeight > 0 && imgHeight > maxHeight) {
            double yRatio = (double) maxHeight / imgHeight;
            if (ratio == null || yRatio < ratio) {
                ratio = yRatio;
            }
        }
        int width = imgWidth;
        int height = imgHeight;
        if (ratio != null) {
            width = (int) (imgWidth * ratio);
            height = (int) (imgHeight * ratio);
            if (width == 0) {
                width = 1;
            }
            if (height == 0) {
                height = 1;
            }
        }
        if (cropHeight > 0 && height > cropHeight) {
            height = cropHeight;
        }
        return new Dimension(width, height);
    }

    public static VFile getVirtualFile(long id, int zoom) {
        com.leaf.uquiz.file.domain.File f = fileService.getFile(id);
        if (f == null) {
            return null;
        }
        Space space = fileService.getSpace(f.getSpaceId());
        VFile original = fileService.getVFile(f);
        VFile decorate = null;
        if (space.getType() == SpaceType.IMAGE) {
            if (zoom == com.leaf.uquiz.file.domain.File.ZOOM_SMALL || zoom == com.leaf.uquiz.file.domain.File.ZOOM_BIG && !StringUtils.equals(f.getExt(), "gif")) {
                List<Zoom> zooms = space.getZooms();
                if (zoom <= zooms.size()) {
                    Zoom z = zooms.get(zoom - 1);
                    decorate = decorateImageVFile(original, z.getWidth(), z.getHeight(), z.getCropHeight());
                }
            }
        }
        return decorate;
    }

    private static VFile decorateImageVFile(VFile vf, int maxWidth, int maxHeight, int cropHeight) {
        VFile tvf = thumbStorage.getVFile(vf.getKey() + "-" + maxWidth + "-" + maxHeight);
        if (!tvf.exist() || tvf.lastModified() < vf.lastModified()) {// 如果不存在或者比原始文件老,就需要重新创建缩略文件
            ImageTransform itf = getImageTransform();
            try {
                itf.load(vf.getInputStream());
                itf.resizeAndcrop(maxWidth, maxHeight, cropHeight);
                itf.save(tvf.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException("resize image [" + vf + "] error");
            }
        }
        return tvf;
    }

    private static ImageTransform getImageTransform() {
        return new AwtImageTransform();
    }
}
