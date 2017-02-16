package com.leaf.uquiz.file.service.impl;

import com.leaf.uquiz.file.ex.FileIOException;
import com.leaf.uquiz.file.service.VFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.channels.WritableByteChannel;

public class FileSystemVfile implements VFile {

    protected String key;
    protected File nativeFile;
    protected String xsendfilePath;

    public FileSystemVfile(String key, File nativeFile, String xsendfilePath) {
        this.key = key;
        this.nativeFile = nativeFile;
        this.xsendfilePath = xsendfilePath;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public long lastModified() {
        return nativeFile.lastModified();
    }

    @Override
    public int getSize() {
        return (int) nativeFile.length();
    }

    @Override
    public boolean exist() {
        return nativeFile.exists();
    }

    @Override
    public boolean remove() {
        return nativeFile.delete();
    }

    @Override
    public InputStream getInputStream() throws FileIOException {
        try {
            return new FileInputStream(nativeFile);
        } catch (FileNotFoundException e) {
            throw new com.leaf.uquiz.file.ex.FileNotFoundException("raw file for key [" + key + "] not found", e);
        }
    }

    @Override
    public OutputStream getOutputStream() throws FileIOException {
        checkAndCreate(true);
        try {
            return new FileOutputStream(nativeFile);
        } catch (FileNotFoundException e) {
            throw new com.leaf.uquiz.file.ex.FileNotFoundException("raw file for key [" + key + "] not found", e);
        }
    }

    @Override
    public void write(byte[] bytes, int position, int length) throws FileIOException {
        checkAndCreate(true);
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(nativeFile, "rw");
            raf.seek(position);
            raf.write(bytes, 0, length);
        } catch (IOException e) {
            throw new FileIOException("write file for key [" + key + "] error", e);
        } finally {
            IOUtils.closeQuietly(raf);
        }
    }

    private void checkAndCreate(boolean createFile) {
        if (!nativeFile.exists()) {
            File dir = nativeFile.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                if (createFile && !nativeFile.createNewFile()) {
                    throw new IOException("create new file error");
                }
            } catch (IOException e) {
                throw new FileIOException("create new file file " + nativeFile.getAbsolutePath() + " error", e);
            }
        }
    }

    @Override
    public int read(byte[] bytes, int position, int length) throws FileIOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(nativeFile, "r");
            raf.seek(position);
            return raf.read(bytes, 0, length);
        } catch (FileNotFoundException e) {
            throw new com.leaf.uquiz.file.ex.FileNotFoundException("raw file for key [" + key + "] not found", e);
        } catch (IOException e) {
            throw new FileIOException("read file for key [" + key + "] error", e);
        } finally {
            IOUtils.closeQuietly(raf);
        }
    }

    @Override
    public void saveFrom(InputStream inputStream) throws FileIOException {
        checkAndCreate(false);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(nativeFile);
            IOUtils.copy(inputStream, out);
        } catch (IOException e) {
            throw new FileIOException("copy file for key [" + key + "] error", e);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public void copyFrom(File file) throws FileIOException {
        checkAndCreate(false);
        try {
            FileUtils.copyFile(file, nativeFile);
        } catch (IOException e) {
            throw new FileIOException("copy file for key [" + key + "] error", e);
        }
    }

    @Override
    public void moveFrom(File file) throws FileIOException {
        checkAndCreate(false);
        try {
            FileUtils.moveFile(file, nativeFile);
        } catch (IOException e) {
            throw new FileIOException("copy file for key [" + key + "] error", e);
        }
    }

    @Override
    public void transferTo(int position, int count, WritableByteChannel target) throws FileIOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(nativeFile);
            fis.getChannel().transferTo(position, count, target);
        } catch (FileNotFoundException e) {
            throw new com.leaf.uquiz.file.ex.FileNotFoundException("raw file for key [" + key + "] not found", e);
        } catch (IOException e) {
            throw new FileIOException("transfer file for key [" + key + "] error", e);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    @Override
    public File getNativeFile() {
        return nativeFile;
    }

    @Override
    public String getXsendfilePath() {
        return xsendfilePath;
    }

    @Override
    public String toString() {
        return "[" + key + ", " + nativeFile.getAbsolutePath() + "]";
    }
}
