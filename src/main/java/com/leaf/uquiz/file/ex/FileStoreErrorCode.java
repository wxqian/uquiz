/*
 * Project:  any
 * Module:   any-file-store
 * File:     ErrorCode.java
 * Modifier: xyang
 * Modified: 2012-07-04 10:36
 *
 * Copyright (c) 2012 Sanyuan Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package com.leaf.uquiz.file.ex;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 12-7-3
 */
public interface FileStoreErrorCode {
    /**
     * 文件存储错误起始值
     */
    int FS_ERROR = 400;

    /**
     * 文件io错误
     */
    int IO_ERROR = FS_ERROR + 1;

    /**
     * 文件未找到错误
     */
    int FILE_NOT_FOUND = FS_ERROR + 2;

    /**
     * 上传文件太大
     */
    int OVER_SIZE = FS_ERROR + 3;

    /**
     * 上传文件类型不被允许
     */
    int EXT_NOT_ALLOW = FS_ERROR + 4;

    /**
     * 空间耗尽
     */
    int SPACE_EXHAUST = FS_ERROR + 5;

    /**
     * 磁盘空间不足
     */
    int CAPABILITY_NOT_ENOUGH = FS_ERROR + 6;

    /**
     * 图片操作错误
     */
    int IMAGE_RESIZE_ERROR = FS_ERROR + 7;
    
    /**
     * 非法的图片尺寸
     */
    int IMAGE_SIZE_NOT_ALLOWED = FS_ERROR + 8;
}
