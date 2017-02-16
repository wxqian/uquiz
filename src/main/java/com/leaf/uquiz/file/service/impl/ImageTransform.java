package com.leaf.uquiz.file.service.impl;


import com.leaf.uquiz.file.domain.Dimension;
import com.leaf.uquiz.file.domain.ImageType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ImageTransform {

    void load(String srcPath) throws IOException;

    void load(InputStream input) throws IOException;

    void save(String destPath) throws IOException;

    void save(OutputStream out) throws IOException;

    boolean isModified();

    Dimension getSize() throws IOException;

    void resize(int width, int height) throws IOException;

    void rotate(double rotate) throws IOException;

    void resizeWithMax(Integer maxWidth, Integer maxHeight) throws IOException;

    void rotateWithMax(double rotate, Integer maxWidth, Integer maxHeight) throws IOException;

    void crop(int left, int top, Integer width, Integer height) throws IOException;

    void resizeAndcrop(int width, int height, int cropHeight) throws IOException;

    InputStream getTransformed() throws IOException;

    ImageType getImageType();
}
