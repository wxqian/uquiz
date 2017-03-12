package com.leaf.uquiz.teacher.service;

import com.leaf.uquiz.core.utils.SessionUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/3/12
 */
@Component
public class ScanHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        HttpSession session = SessionUtils.getSession();
        final String _name = session.getId();
        return new Principal() {
            @Override
            public String getName() {
                return _name;
            }
        };
    }
}
