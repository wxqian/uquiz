/*
 * Project:  any
 * Module:   any-file-store
 * File:     FileIOException.java
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

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 2009-10-23
 */
public class FileIOException extends FileStoreException {

    private static final long serialVersionUID = 927858202290123709L;

    public FileIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileIOException(Integer code, String content) {
        super(code, content);
    }

//    public FileIOException(Throwable cause) {
//        super(IO_ERROR, cause);
//    }
//
//    public FileIOException(String message) {
//        super(IO_ERROR, message);
//    }
//
//    public FileIOException(String message, IOException ex) {
//        super(IO_ERROR, message, ex);
//    }
}
