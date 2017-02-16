package com.leaf.uquiz.file.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Dimension implements Serializable {
    private static final long serialVersionUID = -1806871866141863404L;

    private int width;
    private int height;

    public Dimension() {
    }

    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }


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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
