package com.leaf.uquiz.core.utils;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import static org.apache.commons.io.IOUtils.closeQuietly;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/19.
 */
public class ResponseUtils {
    private static final String INLINE_DISPOSITION_TYPE = "inline";
    private static final String ATTACHMENT_DISPOSITION_TYPE = "attachment";
    private static URLCodec encoder = new URLCodec();

    public static void ok(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public static void notFound(HttpServletResponse response, String error) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        error = StringUtils.defaultString(error);
        try {
            if (!response.isCommitted()) {
                response.getWriter().write(error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void notModified(HttpServletResponse response, String etag) {
        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        if (etag != null) {
            response.setHeader("Etag", etag);
        }
    }

    public static void renderBinary(HttpServletResponse response, InputStream is, String name, boolean inline) {
        if (name != null) {
            setContentTypeIfNotSet(response, MimeTypes.getContentType(name));
        }
        ServletOutputStream out = null;
        try {
            String dispositionType;
            if (inline) {
                dispositionType = INLINE_DISPOSITION_TYPE;
            } else {
                dispositionType = ATTACHMENT_DISPOSITION_TYPE;
            }
            if (!response.getHeaderNames().contains("Content-Disposition")) {
                if (name == null) {
                    response.setHeader("Content-Disposition", dispositionType);
                } else {
                    if (canAsciiEncode(name)) {
                        String contentDisposition = "%s; filename=\"%s\"";
                        response.setHeader("Content-Disposition", String.format(contentDisposition, dispositionType, name));
                    } else {
                        final String encoding = getEncoding(response);
                        String contentDisposition = "%1$s; filename*=" + encoding + "''%2$s; filename=\"%2$s\"";
                        response.setHeader("Content-Disposition", String.format(contentDisposition, dispositionType, encoder.encode(name, encoding)));
                    }
                }
            }
            out = response.getOutputStream();
            IOUtils.copyLarge(is, out);
            out.flush();
        } catch (Exception e) {
            closeQuietly(is);
            closeQuietly(out);
        } finally {
            closeQuietly(is);
            closeQuietly(out);
        }
    }

    public static void setContentTypeIfNotSet(HttpServletResponse response, String contentType) {
        if (response.getContentType() == null) {
            response.setContentType(contentType);
        }
    }

    private static boolean canAsciiEncode(String string) {
        CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();
        return asciiEncoder.canEncode(string);
    }

    protected static String getEncoding(HttpServletResponse response) {
        return response.getCharacterEncoding();
    }
}
