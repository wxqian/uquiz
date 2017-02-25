package com.leaf.uquiz.weixin.message.handler;

import com.leaf.uquiz.core.config.WeixinConfig;
import com.leaf.uquiz.core.utils.RandomStringGenerator;
import com.leaf.uquiz.core.utils.XMLUtil;
import com.leaf.uquiz.weixin.aes.AesException;
import com.leaf.uquiz.weixin.aes.WXBizMsgCrypt;
import com.leaf.uquiz.weixin.message.handler.event.EventHandler;
import com.leaf.uquiz.weixin.message.handler.msg.MsgHandler;
import com.leaf.uquiz.weixin.message.resp.Resp;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/11/8
 */
@Component
public class RequestHandler {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private MsgHandler msgHandler;

    @Autowired
    private WeixinConfig weixinConfig;

    private static final String EVENT = "Event";

    private WXBizMsgCrypt wxBizMsgCrypt;

    @PostConstruct
    public void init() {
        try {
            wxBizMsgCrypt = new WXBizMsgCrypt(weixinConfig.getToken(), weixinConfig.getAesKey(), weixinConfig.getAppId());
        } catch (AesException e) {
            logger.error("init WxBizMsgCrypt occurs error");
        }
    }


    public String handle(Map<String, String> map) {
        Resp resp;
        try {
            if (map.containsKey(EVENT)) {
                resp = eventHandler.handle(map);
            } else {
                resp = msgHandler.handle(map);
            }
            return wxBizMsgCrypt.encryptMsg(XMLUtil.toXML(resp), Long.toString(System.currentTimeMillis()), RandomStringGenerator.getRandomStringByLength(32));
        } catch (Exception e) {
            logger.error("some error occurs when handle message", e);
            return StringUtils.EMPTY;
        }
    }

}
