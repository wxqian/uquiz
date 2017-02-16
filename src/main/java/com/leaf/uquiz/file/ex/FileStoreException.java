/*
 * Project:  any
 * Module:   any-file-store
 * File:     FileStoreException.java
 * Modifier: xyang
 * Modified: 2012-07-04 10:30
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


import com.leaf.uquiz.core.exception.MyException;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 12-7-3
 */
public class FileStoreException extends MyException implements FileStoreErrorCode {
    private static final long serialVersionUID = -82865838657820984L;

    public FileStoreException(String message, Throwable cause) {
        super(message, cause);
    }


    public FileStoreException(Integer code, String content) {
        super(code, content);
    }
}
