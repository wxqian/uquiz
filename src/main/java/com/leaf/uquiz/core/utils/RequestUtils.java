package com.leaf.uquiz.core.utils;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/10/27
 */
public class RequestUtils {
    private static Logger logger = Logger.getLogger(RequestUtils.class);

    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        Assert.notNull(attributes, "could not find current request via RequestContextHolder");
        Assert.isInstanceOf(ServletRequestAttributes.class, attributes);
        HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
        Assert.notNull(request, "could not find current request");
        return request;
    }

    public static String getIP() {
        HttpServletRequest request = getCurrentRequest();
        String xForwardedFor = request.getHeader("$wsra");
        if (xForwardedFor != null) {
            return xForwardedFor;
        }
        xForwardedFor = request.getHeader("X-Real-IP");
        if (xForwardedFor != null) {
            return xForwardedFor;
        }
        xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null) {
            int spaceIndex = xForwardedFor.indexOf(',');
            if (spaceIndex > 0) {
                return xForwardedFor.substring(0, spaceIndex);
            } else {
                return xForwardedFor;
            }
        }
        return request.getRemoteAddr();
    }

    public static String getUserAgent(HttpServletRequest request) {
        return StringUtils.defaultString(request.getHeader("user-agent")).toLowerCase();
    }

    public static boolean isIos(HttpServletRequest request) {
        return getUserAgent(request).contains("iphone");
    }

    public static boolean isAndroid(HttpServletRequest request) {
        return getUserAgent(request).contains("android");
    }

    public static void setCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        //TODO GET domain FROM ENV
        cookie.setMaxAge((int) (DateUtils.MILLIS_PER_DAY * 365 / DateUtils.MILLIS_PER_SECOND));
        response.addCookie(cookie);
    }

    public static String getCookie(String name) {
        return null;
    }

    public static void cacheFor(HttpServletResponse response, String etag, String duration, long lastModified) {
        int maxAge = Time.parseDuration(duration);
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        response.setHeader("Last-Modified", getHttpDateFormatter().format(new Date(lastModified)));
        response.setHeader("Etag", etag);
    }

//    public static ClientType getClientType() {
//        HttpServletRequest request = getCurrentRequest();
//        if (request != null) {
//            String userAgent = getUserAgent(request);
//            if (userAgent.lastIndexOf("mah/") > -1 || userAgent.lastIndexOf("thttpclient") > -1) {//old client UA
//                return ClientType.APP;
//            } else if (userAgent.lastIndexOf("msc/") > -1) {
//                return ClientType.APPBROWSER;
//            } else if (userAgent.lastIndexOf("msb/") > -1) {
//                return ClientType.APPBBROWSER;
//            } else if (userAgent.lastIndexOf("micromessenger") > -1) {
//                return ClientType.WEIXIN;
//            } else if (userAgent.lastIndexOf("alipayclient") > -1) {
//                return ClientType.ALIPAY;
//            } else if (userAgent.lastIndexOf("qq") > -1) {
//                return ClientType.QQ;
//            } else if (userAgent.lastIndexOf("mbah/") > -1) {
//                return ClientType.APPB;
//            } else {
//                return ClientType.BROWSER;
//            }
//        }
//        return ClientType.UNKNOWN;
//    }

    private static ThreadLocal<SimpleDateFormat> httpFormatter = new ThreadLocal<SimpleDateFormat>();

    public static SimpleDateFormat getHttpDateFormatter() {
        if (httpFormatter.get() == null) {
            httpFormatter.set(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US));
            httpFormatter.get().setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        return httpFormatter.get();
    }

    public static String getHeader(String name) {
        return getCurrentRequest().getHeader(name);
    }

    public static boolean hasHeader(String name) {
        return StringUtils.isNotBlank(getHeader(name));
    }

    public static boolean isModified(String etag, long last) {
        if (!(hasHeader("if-none-match") && hasHeader("if-modified-since"))) {
            return true;
        } else {
            String browserEtag = getHeader("if-none-match");
            if (!browserEtag.equals(etag)) {
                return true;
            } else {
                try {

                    Date browserDate = getHttpDateFormatter().parse(getHeader("if-modified-since"));
                    if (browserDate.getTime() >= last) {
                        return false;
                    }
                } catch (ParseException ex) {
                    logger.error("Can't parse date", ex);
                }
                return true;
            }
        }
    }

    private static URLCodec encoder = new URLCodec();

    public static void downloadConfig(HttpServletRequest request, HttpServletResponse response, String fileName, boolean isInline) {
        response.setHeader("Content-Disposition",
                generateContentDisposition(fileName, isInline ? "inline" : "attachment", request.getCharacterEncoding()));
        response.setContentType(MimeTypes.getContentType(fileName));
    }

    public static void attachment(HttpServletRequest request, HttpServletResponse response, String fileName) {
        downloadConfig(request, response, fileName, false);
    }

    public static void inline(HttpServletRequest request, HttpServletResponse response, String fileName) {
        downloadConfig(request, response, fileName, true);
    }

    private static String generateContentDisposition(String name, String dispositionType, String encoding) {
        try {
            if (canAsciiEncode(name)) {
                String contentDisposition = "%s; filename=\"%s\"";
                return String.format(contentDisposition, dispositionType, name);
            } else {
                String contentDisposition = "%1$s; filename*=" + encoding + "''%2$s; filename=\"%2$s\"";
                return String.format(contentDisposition, dispositionType, encoder.encode(name, encoding));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean canAsciiEncode(String string) {
        CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();
        return asciiEncoder.canEncode(string);
    }

    private static void notFound(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    /**
     * 获得请求中所带的参数
     *
     * @param request
     * @return
     */
    public static List<String> getRequestParam(HttpServletRequest request) {
        List<String> urlParams = new ArrayList<String>();
        Map params = request.getParameterMap();
        Iterator it = params.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            String[] values = (String[]) params.get(key);
            if (values != null && values.length > 0) {
                urlParams.add(values[0]);
            } else {

            }
        }

        return urlParams;
    }
}
