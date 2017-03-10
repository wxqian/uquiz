package com.leaf.uquiz.file.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件模块的配置.
 */
@ConfigurationProperties(prefix = "file.config")
public class FileSettings {
    private boolean useXsendfile;
    private String mainPath;
    private String thumbPath;
    private String amrPath;
    private String mp3Path;

    public boolean isUseXsendfile() {
        return useXsendfile;
    }

    public void setUseXsendfile(boolean useXsendfile) {
        this.useXsendfile = useXsendfile;
    }

    public String getMainPath() {
        return mainPath;
    }

    public void setMainPath(String mainPath) {
        this.mainPath = mainPath;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getAmrPath() {
        return amrPath;
    }

    public void setAmrPath(String amrPath) {
        this.amrPath = amrPath;
    }

    public String getMp3Path() {
        return mp3Path;
    }

    public void setMp3Path(String mp3Path) {
        this.mp3Path = mp3Path;
    }
}
