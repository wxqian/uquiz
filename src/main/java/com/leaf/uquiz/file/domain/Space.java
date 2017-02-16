package com.leaf.uquiz.file.domain;


import com.google.common.collect.Lists;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Space implements Serializable {
    private static final long serialVersionUID = -2052685861263989527L;

//空间编号
    private int id;

//空间key
    @Column(name = "key0")
    private String key;

//空间类型
    private SpaceType type = SpaceType.BIN;

//名称
    private String name;

//最大允许文件大小
    private int maxSize = -1;

//允许的后缀
    private Set<String> allowExts;

//默认文件路径
    private String defaultImage;

//缩放级别
    private List<Zoom> zooms = Lists.newArrayList();

//初始化缩放
    private Dimension initResize;

//文件缓存秒数
    private int cacheSeconds = 15552000;//180d

//限速,单位byte/s
    private int limitRate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SpaceType getType() {
        return type;
    }

    public void setType(SpaceType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public Set<String> getAllowExts() {
        return allowExts;
    }

    public void setAllowExts(Set<String> allowExts) {
        this.allowExts = allowExts;
    }
    public Space defaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
        return this;
    }

    public List<Zoom> getZooms() {
        return zooms;
    }

    public void setZooms(List<Zoom> zooms) {
        this.zooms = zooms;
    }

    public Dimension getInitResize() {
        return initResize;
    }

    public void setInitResize(Dimension initResize) {
        this.initResize = initResize;
    }

    public int getCacheSeconds() {
        return cacheSeconds;
    }

    public void setCacheSeconds(int cacheSeconds) {
        this.cacheSeconds = cacheSeconds;
    }

    public int getLimitRate() {
        return limitRate;
    }

    public void setLimitRate(int limitRate) {
        this.limitRate = limitRate;
    }

    public String getDefaultImage() {
        return defaultImage;
    }
}
