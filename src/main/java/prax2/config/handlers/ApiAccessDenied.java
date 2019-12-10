package prax2.config.handlers;

import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiAccessDenied implements org.springframework.security.web.access.AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response
            , AccessDeniedException accessDeniedException) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
