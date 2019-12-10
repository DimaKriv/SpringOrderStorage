package prax2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import prax2.config.filters.ApiAuthenticationFilter;
import prax2.config.filters.ApiAutorizationFilter;
import prax2.config.handlers.ApiAccessDenied;
import prax2.config.handlers.ApiEntryPoint;
import prax2.config.handlers.ApiLogoutSuccess;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"prax2.orderdao.database"})
@PropertySource("classpath:/application.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Value("${jwtKey}")
    private String jwtKey = "r6m4GNX6voKiPh5pfCaWkQoG8d1E756ioKiPh2pfCaWk59ioKiPh2h5pfCaWkQoG8d1E756io";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/login").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/version").permitAll()
                .antMatchers("/api/users").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/**").authenticated();
        http.logout().logoutSuccessHandler(new ApiLogoutSuccess());
        http.exceptionHandling().authenticationEntryPoint(new ApiEntryPoint());
        http.exceptionHandling().accessDeniedHandler(new ApiAccessDenied());
        var apiAuthenticationFilter = new ApiAuthenticationFilter(authenticationManager()
                , "/api/login", jwtKey);
        http.addFilterAfter(apiAuthenticationFilter, LogoutFilter.class);
        var apiAuthorizationFilter = new ApiAutorizationFilter(authenticationManager(), jwtKey);
        http.addFilterBefore(apiAuthorizationFilter, LogoutFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.jdbcAuthentication().dataSource(dataSource).passwordEncoder(new BCryptPasswordEncoder());
    }
}
