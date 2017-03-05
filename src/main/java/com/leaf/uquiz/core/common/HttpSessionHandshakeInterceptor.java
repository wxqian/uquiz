package com.leaf.uquiz.core.common;

import com.leaf.uquiz.core.utils.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/3/4
 */
public class HttpSessionHandshakeInterceptor implements HandshakeInterceptor {

    public static final String HTTP_SESSION_ID_ATTR_NAME = "HTTP.SESSION.ID";
    private Logger logger = LoggerFactory.getLogger(HttpSessionHandshakeInterceptor.class);
    private final Collection<String> attributeNames;
    private boolean copyAllAttributes;
    private boolean copyHttpSessionId = true;
    private boolean createSession;

    public HttpSessionHandshakeInterceptor() {
        this.attributeNames = Collections.emptyList();
        this.copyAllAttributes = true;
    }

    public HttpSessionHandshakeInterceptor(Collection<String> attributeNames) {
        this.attributeNames = Collections.unmodifiableCollection(attributeNames);
        this.copyAllAttributes = false;
    }

    public Collection<String> getAttributeNames() {
        return this.attributeNames;
    }

    public void setCopyAllAttributes(boolean copyAllAttributes) {
        this.copyAllAttributes = copyAllAttributes;
    }

    public boolean isCopyAllAttributes() {
        return this.copyAllAttributes;
    }

    public void setCopyHttpSessionId(boolean copyHttpSessionId) {
        this.copyHttpSessionId = copyHttpSessionId;
    }

    public boolean isCopyHttpSessionId() {
        return this.copyHttpSessionId;
    }

    public void setCreateSession(boolean createSession) {
        this.createSession = createSession;
    }

    public boolean isCreateSession() {
        return this.createSession;
    }

    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpSession session = SessionUtils.getSession();
        logger.info("sessionId:{}",session.getId());
        if (session != null) {
            if (this.isCopyHttpSessionId()) {
                attributes.put("HTTP.SESSION.ID", session.getId());
            }

            Enumeration names = session.getAttributeNames();

            while (true) {
                String name;
                do {
                    if (!names.hasMoreElements()) {
                        return true;
                    }

                    name = (String) names.nextElement();
                } while (!this.isCopyAllAttributes() && !this.getAttributeNames().contains(name));


                attributes.put(name, session.getAttribute(name));
            }
        } else {
            return true;
        }
    }

    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
    }
}
