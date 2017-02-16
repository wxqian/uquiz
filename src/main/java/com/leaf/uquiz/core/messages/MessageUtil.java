package com.leaf.uquiz.core.messages;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/**
 * i18n 消息获取
 *
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/16
 */
@Component
public class MessageUtil implements MessageSourceAware {

    private static MessageSourceAccessor accessor;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        MessageUtil.accessor = new MessageSourceAccessor(messageSource);
    }

    public static String getMessage(String code, Object... args) {
        return accessor.getMessage(code, args);
    }
}
