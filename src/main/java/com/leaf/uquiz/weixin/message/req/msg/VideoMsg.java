package com.leaf.uquiz.weixin.message.req.msg;


import com.leaf.uquiz.weixin.message.req.Req;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/16
 */
public class VideoMsg extends Req {
    public VideoMsg() {
        MsgType = com.leaf.uquiz.weixin.message.req.msg.MsgType.video;
    }
}
