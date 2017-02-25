package com.leaf.uquiz.weixin.message.req.msg;


import com.leaf.uquiz.weixin.message.req.Req;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2014/12/2
 */
public class LocationMsg extends Req {
    public LocationMsg() {
        MsgType = com.leaf.uquiz.weixin.message.req.msg.MsgType.location;
    }
}
