package com.leaf.uquiz.core.utils;

import org.apache.commons.io.Charsets;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/27.
 */
public class IOUtils extends org.apache.commons.io.IOUtils {

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    public static InputStream load(String location) {
        try {
            return resourceLoader.getResource(location).getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(String location) {
        InputStream is = null;
        try {
            is = load(location);
            return org.apache.commons.io.IOUtils.toString(is, Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException();
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(is);
        }
    }

}
