package dev.abrandell.api.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.abrandell.api.account.AuthRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            AuthRequest req = new ObjectMapper()
                .readValue(request.getInputStream(), AuthRequest.class);

            LOGGER.debug(req.toString());
            var authRequest = new UsernamePasswordAuthenticationToken(
                req.getUsername(),
                req.getPassword()
            );

            return authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate a random UUID string to parse in both the JWT payload (HttpOnly cookie) and a header. Header must match
     * the HttpOnly JWT cookie payload claim 'CSRF' on attempts to access protected resources after successful
     * authentication.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) {
        LOGGER.debug("Successfully authenticated, creating CSRF token...");

        final String csrfToken = java.util.UUID.randomUUID().toString();
        LOGGER.debug("Created CSRF token: {}\nCreating JWT...", csrfToken);

        final String jwt = jwtService.generateToken(authentication, csrfToken);
        LOGGER.debug("Created JWT: {}", jwt);

        final Cookie cookie = jwtService.createJwtCookie(jwt);
        LOGGER.debug("Created JWT Cookie: {}: {}", cookie.getName(), cookie.getValue());

        response.addHeader("CSRF", csrfToken);
        response.addCookie(cookie);
    }


}
