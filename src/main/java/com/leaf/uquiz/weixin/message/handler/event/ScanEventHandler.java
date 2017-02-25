package com.leaf.uquiz.weixin.message.handler.event;


import com.leaf.uquiz.weixin.message.req.Req;
import com.leaf.uquiz.weixin.message.req.event.EventType;
import com.leaf.uquiz.weixin.message.resp.Resp;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/14
 */
@Component
public class ScanEventHandler extends EventHandler {

    @Override
    protected Resp handleInternal(Req req) {
        return null;
    }

    @Override
    protected EventType support() {
        return EventType.SCAN;
    }
}
