package com.leaf.uquiz.weixin.message.req.msg;


import com.leaf.uquiz.core.enums.Description;
import com.leaf.uquiz.core.enums.Titleable;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2014/12/2
 */
@Description("消息类型")
public enum MsgType implements Titleable {
    text("文本消息"), image("图片消息"), voice("语音消息"), video("视频消息"), location("地理位置消息"), link("链接消息"), event("事件消息");

    String title;

    MsgType(String title) {
        this.title = title;
    }

    public static MsgType findByName(String name) {
        if ("text".equals(name)) {
            return text;
        } else if ("image".equals(name)) {
            return image;
        } else if ("voice".equals(name)) {
            return voice;
        } else if ("video".equals(name)) {
            return video;
        } else if ("location".equals(name)) {
            return location;
        } else if ("event".equals(name)) {
            return event;
        } else {
            return link;
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    public static Class findClazz(MsgType type) {
        int order = type.ordinal();
        if (order == 0) {
            return TextMsg.class;
        } else if (order == 1) {
            return ImageMsg.class;
        } else if (order == 2) {
            return VoiceMsg.class;
        } else if (order == 3) {
            return VideoMsg.class;
        } else if (order == 4) {
            return LocationMsg.class;
        } else {
            return LinkMsg.class;
        }
    }
}
