package com.leaf.uquiz.weixin.message.handler.msg;

import com.alibaba.fastjson.util.TypeUtils;
import com.leaf.uquiz.core.exception.MyException;
import com.leaf.uquiz.weixin.message.req.Req;
import com.leaf.uquiz.weixin.message.req.msg.MsgType;
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
public class MsgHandler implements ApplicationContextAware {
    private Iterable<MsgHandler> msgHandlers;

    public Resp handle(Map<String, String> map) {
        MsgType type = MsgType.findByName(map.get("MsgType"));
        Class clazz = MsgType.findClazz(type);
        Req req = (Req) TypeUtils.castToJavaBean(map, clazz);
        MsgHandler handler = findHandler(type);
        return handler.handleInternal(req);
    }

    private MsgHandler findHandler(MsgType type) {
        MsgHandler handler = null;
        for (MsgHandler handler1 : msgHandlers) {
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

    protected MsgType support() {
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        msgHandlers = applicationContext.getBeansOfType(MsgHandler.class).values();
    }
}
