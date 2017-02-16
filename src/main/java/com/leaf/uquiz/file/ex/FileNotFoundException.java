/*
 * Project:  any
 * Module:   any-file-store
 * File:     FileNotFoundException.java
 * Modifier: xyang
 * Modified: 2012-07-04 10:39
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
public class FileNotFoundException extends FileStoreException {

    private static final long serialVersionUID = 927858202290123709L;

    public FileNotFoundException(String message) {
        super(FILE_NOT_FOUND, message);
    }

    public FileNotFoundException(String s, java.io.FileNotFoundException e) {
        super(s, e);
    }

//    public FileNotFoundException(String message, IOException ex) {
//        super(FILE_NOT_FOUND, message, ex);
//    }
}
