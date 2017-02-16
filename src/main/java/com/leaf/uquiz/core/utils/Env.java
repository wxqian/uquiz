package com.leaf.uquiz.core.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/1
 */
@Component
public class Env {

    private static final String BaseUrl = "http://activity.ttgoing.com";

    @Value("system.dev-mode")
    private static boolean devMode;

    public Env() {

    }

    public static boolean isDev() {
        return devMode;
    }

    public static String url(String url) {
        if (url.startsWith("http")) {
            return url;
        }
        return BaseUrl + format(url);
    }

    private static String format(String url) {
        return url.charAt(0) == '/' ? url : "/" + url;
    }
}
