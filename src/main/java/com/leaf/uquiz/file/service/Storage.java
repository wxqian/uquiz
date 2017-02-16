package com.leaf.uquiz.file.service;



public interface Storage extends Comparable<Storage> {

    void init();

    void destroy();

    String getId();

    String getType();

    long getCapability();

    long getUsed();

    boolean isAvailable();

    boolean contains(String spaceKey, String fileKey);

    boolean contains(String fileKey);

    boolean remove(String spaceKey, String fileKey);

    boolean remove(String fileKey);

    VFile getVFile(String spaceKey, String fileKey);

    VFile getVFile(String fileKey);
}
