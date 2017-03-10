package com.leaf.uquiz.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/10
 */
@ConfigurationProperties(prefix = "uquiz.kaptcha")
public class KaptchaConfig {
    private String border;

    private String sessionKey;

    private String fontColor;

    private String fontSize;

    private String obscurificator;

    private String noise;

    private String width;

    private String height;

    private String charLength;

    private String charSpace;

    private String from;

    private String to;

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getCharLength() {
        return charLength;
    }

    public void setCharLength(String charLength) {
        this.charLength = charLength;
    }

    public String getCharSpace() {
        return charSpace;
    }

    public void setCharSpace(String charSpace) {
        this.charSpace = charSpace;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getNoise() {
        return noise;
    }

    public void setNoise(String noise) {
        this.noise = noise;
    }

    public String getObscurificator() {
        return obscurificator;
    }

    public void setObscurificator(String obscurificator) {
        this.obscurificator = obscurificator;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
