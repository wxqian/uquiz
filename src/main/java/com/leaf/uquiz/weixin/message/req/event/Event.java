package com.leaf.uquiz.weixin.message.req.event;


import com.leaf.uquiz.weixin.message.req.Req;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2014/12/2
 */
public class Event extends Req {
    public EventType Event;

    public String EventKey;

    public Event() {
        MsgType = com.leaf.uquiz.weixin.message.req.msg.MsgType.event;
    }


}
