package prax2.config.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import prax2.orderpojo.LoginCredential;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

public class ApiAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private String jwtKey;
    private final int tokenTime = 1000 * 60 * 15;

    public ApiAuthenticationFilter(AuthenticationManager authenticationManager, String defaultFilterProcessesUrl
            , String jwtKey) {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(authenticationManager);
        setAuthenticationFailureHandler(
                (request, response, exception) -> response.setStatus(HttpServletResponse.SC_UNAUTHORIZED));
        setAuthenticationSuccessHandler(
                (request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK));
        this.jwtKey = jwtKey;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        try {
            ObjectMapper ob = new ObjectMapper();
            LoginCredential  loginCredentials =  ob.readValue(request.getReader(), LoginCredential.class);
            var token = new UsernamePasswordAuthenticationToken(
                            loginCredentials.getUserName(),
                            loginCredentials.getPassword());
            return getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain,
                                    Authentication authentication) throws IOException, ServletException {
        String token  = Jwts.builder().signWith(Keys.hmacShaKeyFor(jwtKey.getBytes()), SignatureAlgorithm.HS512)
                .setSubject(authentication.getName())
                .claim("roles", authentication.getAuthorities().stream()
                        .map(r -> r.getAuthority()).collect(Collectors.joining(",")))
                .setExpiration(new Date(Calendar.getInstance().getTimeInMillis() + tokenTime))
                .compact();

        response.addHeader("Authorization", "Bearer " + token);
    }
}
