package prax2.config.handlers;

import org.springframework.security.core.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException authException) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
