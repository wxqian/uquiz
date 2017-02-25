package com.leaf.uquiz.weixin.message.req;


import com.leaf.uquiz.weixin.message.req.msg.MsgType;

import java.io.Serializable;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2014/12/2
 */
public class Req implements Serializable {

    public String ToUserName;

    public String FromUserName;

    public MsgType MsgType;

    public long CreateTime;

    public long MsgId;
}
