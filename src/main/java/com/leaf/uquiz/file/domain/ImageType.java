package com.leaf.uquiz.file.domain;


import org.apache.commons.io.FilenameUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 13-11-22
 */
public enum ImageType {
    JPG("jpg", "jpeg"),
    PNG("png"),
    GIF("gif"),
    BMP("bmp");

    private String[] exts;
    private static Map<String, ImageType> MAPPING = new HashMap<String, ImageType>();

    static {
        for (ImageType it : values()) {
            for (String ext : it.exts) {
                MAPPING.put(ext, it);
            }
        }
    }

    ImageType(String... exts) {
        this.exts = exts;
    }

    public String getExt() {
        return exts[0];
    }

    public static ImageType from(String fileName) {
        return fromExt(FilenameUtils.getExtension(fileName));
    }

    public static ImageType fromExt(String ext) {
        return MAPPING.get(ext.toLowerCase());
    }
}
