package re1kur.app.service.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import re1kur.app.dto.websocket.ChatPrincipal;

import java.security.Principal;
import java.util.Map;

@Component
public class HandshakeAuthInterceptor implements HandshakeInterceptor {

    public static final String PRINCIPAL_ATTRIBUTE = "chatPrincipal";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            Principal principal = servletRequest.getServletRequest().getUserPrincipal();
            if (principal instanceof OAuth2AuthenticationToken token
                    && token.getPrincipal() instanceof OidcUser user) {
                String name = user.getFullName() != null ? user.getFullName()
                        : user.getPreferredUsername() != null ? user.getPreferredUsername()
                        : user.getEmail();
                attributes.put(PRINCIPAL_ATTRIBUTE, new ChatPrincipal(user.getSubject(), name, false));
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                              WebSocketHandler wsHandler, Exception exception) {
    }
}
