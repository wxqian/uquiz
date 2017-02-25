package com.leaf.uquiz.weixin.message.resp;


import com.leaf.uquiz.core.enums.RespType;
import com.leaf.uquiz.weixin.message.resp.item.Voice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/9
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class VoiceResp extends Resp {
    public VoiceResp() {
        MsgType = RespType.voice;
    }

    @XmlElement(name = "Voice")
    public Voice Voice;
}
