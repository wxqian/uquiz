package com.leaf.uquiz.weixin.message.resp;


import com.leaf.uquiz.core.enums.RespType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/8
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class TextResp extends Resp {
    public TextResp() {
        MsgType = RespType.text;
    }

    @XmlElement(name = "Content")
    public String Content;
}
