package com.leaf.uquiz.teacher.controller;

import com.leaf.uquiz.core.cache.StringCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/3/4
 */
@Controller
public class WsController {

    private Logger logger = LoggerFactory.getLogger(WsController.class);

    @Autowired
    private StringCache stringCache;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/time")
    public void sendTime(Principal principal, String message) {
        String sendTime = stringCache.get("sendTime");
        logger.info("username", principal.getName());
        if (StringUtils.isBlank(sendTime)) {
            logger.info("aaaaaaaaaa");
            messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/notifications", message);
        }
        messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/notifications", "aaaaaa");
    }

}
