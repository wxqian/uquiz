package com.leaf.uquiz.file.domain;


import com.leaf.uquiz.core.enums.Titleable;

public enum SpaceType implements Titleable {
    BIN("普通文件"),
    IMAGE("图片"),
    VIDEO("视频"),
    AUDIO("音频");

    private String title;

    SpaceType(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
