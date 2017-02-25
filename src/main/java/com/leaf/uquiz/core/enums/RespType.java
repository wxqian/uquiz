package com.leaf.uquiz.core.enums;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/8
 */
@Description("回复消息类型")
public enum RespType implements Titleable {
    text("文本消息"), image("图片消息"), voice("语音消息"),
    video("视频消息"), music("音乐消息"), news("图文消息");
    String title;

    RespType(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
