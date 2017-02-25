package com.leaf.uquiz.weixin.message.req.msg;


import com.leaf.uquiz.weixin.message.req.Req;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2014/12/2
 */
public class LinkMsg extends Req {

    public LinkMsg(){
        MsgType = com.leaf.uquiz.weixin.message.req.msg.MsgType.link;
    }
}
