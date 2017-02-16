package com.leaf.uquiz.core.utils;


import javax.servlet.http.HttpSession;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/10/27
 */
public class SessionUtils {

    public static HttpSession getSession() {
        return RequestUtils.getCurrentRequest().getSession(true);
    }

}
