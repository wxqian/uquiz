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
}
