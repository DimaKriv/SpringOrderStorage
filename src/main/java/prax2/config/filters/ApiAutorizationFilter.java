package prax2.config.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ApiAutorizationFilter extends BasicAuthenticationFilter {

    private String jwtKey;

    public ApiAutorizationFilter(AuthenticationManager authenticationManager, String jwtKey) {
        super(authenticationManager);
        this.jwtKey = jwtKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String tokenString = request.getHeader("Authorization");
        if (tokenString != null) {
                tokenString = tokenString.replaceFirst("Bearer ", "");
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtKey.getBytes()).parseClaimsJws(tokenString).getBody();

                List<SimpleGrantedAuthority> authorities = List.of(claims.get("roles", String.class)
                        .split(",")).stream().map(r -> new SimpleGrantedAuthority(r)).collect(Collectors.toList());

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities));
        }
        chain.doFilter(request, response);
    }
}
