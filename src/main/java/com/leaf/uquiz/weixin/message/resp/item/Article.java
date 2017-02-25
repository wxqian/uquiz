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
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Article {

    @XmlElement(name = "Title")
    public String Title;

    @XmlElement(name = "Description")
    public String Description;

    @XmlElement(name = "PicUrl")
    public String PicUrl;

    @XmlElement(name = "Url")
    public String Url;
}
