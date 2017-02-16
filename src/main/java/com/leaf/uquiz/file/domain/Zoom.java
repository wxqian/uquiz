package com.leaf.uquiz.file.domain;

import java.io.Serializable;

public class Zoom implements Serializable {
    private static final long serialVersionUID = 2543975225309429093L;

    private int width;
    private int height;

    public Zoom(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private int cropHeight;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCropHeight() {
        return cropHeight;
    }

    public void setCropHeight(int cropHeight) {
        this.cropHeight = cropHeight;
    }
}
