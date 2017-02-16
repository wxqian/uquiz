package com.leaf.uquiz.file.service.impl;

import com.leaf.uquiz.file.service.Storage;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractStorage implements Storage {

    private String id;
    private String type;
    private int order = 100;
    private long capability = -1;
    private AtomicLong used = new AtomicLong(0);
    private boolean enable = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public long getCapability() {
        return capability;
    }

    public void setCapability(long capability) {
        this.capability = capability;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getUsed() {
        return used.longValue();
    }

    @Override
    public boolean isAvailable() {
        return enable && (getCapability() < 0 || getCapability() > getUsed());
    }

    @Override
    public int compareTo(Storage storage) {
        return order - ((AbstractStorage) storage).getOrder();
    }


    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
