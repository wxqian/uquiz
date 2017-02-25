package com.leaf.uquiz.weixin.message.handler.event;

import com.alibaba.fastjson.util.TypeUtils;
import com.leaf.uquiz.core.exception.MyException;
import com.leaf.uquiz.weixin.message.req.Req;
import com.leaf.uquiz.weixin.message.req.event.EventType;
import com.leaf.uquiz.weixin.message.resp.Resp;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/8
 */
@Component
public class EventHandler implements ApplicationContextAware {
    private Iterable<EventHandler> eventHandlers;

    public Resp handle(Map<String, String> map) {
        EventType type = EventType.findByName(map.get("Event"));
        EventHandler handler = findHandler(type);
        Class clazz = EventType.findClazz(type);
        Req req = (Req) TypeUtils.castToJavaBean(map, clazz);
        return handler.handleInternal(req);
    }

    private EventHandler findHandler(EventType type) {
        EventHandler handler = null;
        for (EventHandler handler1 : eventHandlers) {
            if (type.equals(handler1.support())) {
                handler = handler1;
            }
        }
        if (handler == null) {
            throw new MyException("There is no handler support for this EventType:" + type.getTitle());
        }
        return handler;
    }

    protected Resp handleInternal(Req req) {
        return new Resp();
    }

    protected EventType support() {
        return null;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.eventHandlers = applicationContext.getBeansOfType(EventHandler.class).values();
    }
}
