package com.leaf.uquiz.weixin.message.resp;


import com.leaf.uquiz.core.enums.RespType;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/8
 */
public class Resp {
    @XmlElement(name = "ToUserName")
    public String ToUserName;

    @XmlElement(name = "FromUserName")
    public String FromUserName;

    @XmlElement(name = "MsgType")
    public RespType MsgType;

    @XmlElement(name = "CreateTime")
    public long CreateTime;
}
