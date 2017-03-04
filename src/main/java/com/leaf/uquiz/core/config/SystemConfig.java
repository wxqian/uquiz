package com.leaf.uquiz.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/27
 */
@ConfigurationProperties(prefix = "system")
public class SystemConfig {
    private boolean devMode;
    private String baseUrl;
    private String frontEndUrl;
    private String mobileEndUrl;

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getFrontEndUrl() {
        return frontEndUrl;
    }

    public void setFrontEndUrl(String frontEndUrl) {
        this.frontEndUrl = frontEndUrl;
    }

    public String getMobileEndUrl() {
        return mobileEndUrl;
    }

    public void setMobileEndUrl(String mobileEndUrl) {
        this.mobileEndUrl = mobileEndUrl;
    }
}
