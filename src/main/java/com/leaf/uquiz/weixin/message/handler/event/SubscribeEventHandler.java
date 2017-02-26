package com.leaf.uquiz.weixin.message.handler.event;

import com.leaf.uquiz.teacher.service.TeacherService;
import com.leaf.uquiz.weixin.message.req.Req;
import com.leaf.uquiz.weixin.message.req.event.EventType;
import com.leaf.uquiz.weixin.message.req.event.SubscribeEvent;
import com.leaf.uquiz.weixin.message.resp.Resp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/14
 */
@Component
public class SubscribeEventHandler extends EventHandler {

    @Autowired
    private TeacherService teacherService;

    @Override
    protected Resp handleInternal(Req req) {
        SubscribeEvent event = (SubscribeEvent) req;
        String ticket = event.Ticket;
        //若ticket为空,则为关注事件,否则则是扫描带参数二维码事件
        if (StringUtils.isBlank(ticket)) {
            return teacherService.scanLogin(event.FromUserName, event.ToUserName, ticket);
        }
        return null;
    }

    @Override
    protected EventType support() {
        return EventType.subscribe;
    }
}
