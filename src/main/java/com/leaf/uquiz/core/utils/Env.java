package com.leaf.uquiz.core.utils;

import com.leaf.uquiz.core.config.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/1
 */
@Component
public class Env {

    @Autowired
    private static SystemConfig systemConfig;


    public Env() {
    }

    public static String url(String url) {
        if (url.startsWith("http")) {
            return url;
        }
        return systemConfig.getBaseUrl() + format(url);
    }

    private static String format(String url) {
        return url.charAt(0) == '/' ? url : "/" + url;
    }
}
