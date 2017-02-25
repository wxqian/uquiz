package com.leaf.uquiz.weixin.message.req.event;


import com.leaf.uquiz.core.enums.Description;
import com.leaf.uquiz.core.enums.Titleable;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2014/12/2
 */
@Description("事件类型")
public enum EventType implements Titleable {
    subscribe("关注/取消关注"), SCAN("扫描二维码"), LOCATION("地理位置"), CLICK("点击"), VIEW("跳转链接");

    String title;

    EventType(String title) {
        this.title = title;
    }


    public static EventType findByName(String name) {
        if ("subscribe".equals(name)) {
            return subscribe;
        } else if ("SCAN".equals(name)) {
            return SCAN;
        } else if ("LOCATION".equals(name)) {
            return LOCATION;
        } else if ("CLICK".equals(name)) {
            return CLICK;
        } else {
            return VIEW;
        }
    }

    @Override
    public String getTitle() {
        return title;
    }


    public static Class findClazz(EventType type) {
        int order = type.ordinal();
        if (order == 0) {
            return SubscribeEvent.class;
        } else if (order == 1) {
            return ScanEvent.class;
        } else if (order == 2) {
            return LocationEvent.class;
        } else if (order == 3) {
            return ClickEvent.class;
        } else {
            return ViewEvent.class;
        }

    }
}
