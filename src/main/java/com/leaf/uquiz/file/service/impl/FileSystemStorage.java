package com.leaf.uquiz.file.service.impl;

import com.leaf.uquiz.core.utils.Codecs;
import com.leaf.uquiz.file.service.VFile;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileSystemStorage extends AbstractStorage {

    public static final String MAIN = "main";
    private static final Logger LOG = LoggerFactory.getLogger(FileSystemStorage.class);
    private File directory;
    private boolean useXsendfile;

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public void setUseXsendfile(boolean useXsendfile) {
        this.useXsendfile = useXsendfile;
    }

    @Override
    public synchronized void init() {
        if (directory.exists()) {
            setEnable(true);
            LOG.info("Use storage [" + this + "]");
        } else {
            LOG.info("FileSystemStorage path [" + directory.getAbsolutePath() + "] is not exist,create it");
            directory.mkdirs();
            setEnable(true);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean contains(String spaceKey, String fileKey) {
        return getRawFile(spaceKey, fileKey).exists();
    }

    @Override
    public boolean contains(String fileKey) {
        return contains(MAIN, fileKey);
    }

    @Override
    public boolean remove(String spaceKey, String fileKey) {
        return getRawFile(spaceKey, fileKey).delete();
    }

    @Override
    public boolean remove(String fileKey) {
        return remove(MAIN, fileKey);
    }

    @Override
    public VFile getVFile(String spaceKey, String fileKey) {
        String path = getPath(spaceKey, fileKey);
        String xpath = null;
        if (useXsendfile) {
            StringBuilder sb = new StringBuilder("/");
            sb.append(getId()).append("/").append(path);
            xpath = sb.toString();
        }
        return new FileSystemVfile(fileKey, new File(directory, path), xpath);
    }

    @Override
    public VFile getVFile(String fileKey) {
        return getVFile(MAIN, fileKey);
    }

    private File getRawFile(String spaceKey, String fileKey) {
        return new File(directory, getPath(spaceKey, fileKey));
    }

    private String getPath(String spaceKey, String fileKey) {
        if (spaceKey == null || fileKey == null) {
            throw new IllegalArgumentException("spaceKey and fileKey cannot be null");
        }
        
        StringBuilder path = new StringBuilder(spaceKey);
        String hash = Codecs.md5Hex(fileKey);
        path.append(File.separator);
        path.append(StringUtils.substring(hash, 0, 2));
        path.append(File.separator);
        path.append(StringUtils.substring(hash, 2, 4));
        path.append(File.separator);
        path.append(hash);
        
        return path.toString();
    }
}
