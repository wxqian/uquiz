package com.leaf.uquiz.weixin.message.resp.item;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/9
 */
@XmlRootElement(name = "Image")
@XmlAccessorType(XmlAccessType.FIELD)
public class Image {

    @XmlElement(name = "MediaId")
    public String MediaId;
}
