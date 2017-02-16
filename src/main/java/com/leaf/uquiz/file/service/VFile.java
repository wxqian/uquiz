package com.leaf.uquiz.file.service;


import com.leaf.uquiz.file.ex.FileIOException;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.WritableByteChannel;

public interface VFile {

    String getKey();

    long lastModified();

    int getSize();

    boolean exist();

    boolean remove();

    InputStream getInputStream() throws FileIOException;

    OutputStream getOutputStream() throws FileIOException;

    void write(byte[] bytes, int position, int length) throws FileIOException;

    int read(byte[] bytes, int position, int length) throws FileIOException;

    void saveFrom(InputStream inputStream) throws FileIOException;

    void copyFrom(File file) throws FileIOException;

    void moveFrom(File file) throws FileIOException;

    void transferTo(int position, int count, WritableByteChannel target) throws FileIOException;

    File getNativeFile();

    String getXsendfilePath();
}
