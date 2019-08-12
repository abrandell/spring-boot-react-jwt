package dev.abrandell.api.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.abrandell.api.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private final AccountService accountService;
    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  AccountService accountService, JwtService jwtService) {
        super(authenticationManager);
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        jwtService.fetchCsrfHeader(request)
            .ifPresentOrElse((final String csrfHeader) -> {
                LOGGER.debug("CSRF header found: {}", csrfHeader);
                SecurityContextHolder.getContext()
                    .setAuthentication(this.createAuth(csrfHeader, request));
            }, () -> LOGGER.debug("CSRF header is either null or empty"));

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken createAuth(final String csrfHeader, HttpServletRequest request) {
        final Cookie jwtCookie = jwtService.fetchJwtCookie(request.getCookies())
            .orElseThrow(() -> new InsufficientAuthenticationException("Missing JWT cookie"));
        final DecodedJWT decodedJWT = jwtService.decodeAndVerifyJWT(jwtCookie);
        final String username = decodedJWT.getSubject();

        if (hasText(username) && jwtService.csrfHeaderMatchesJwtClaim(csrfHeader, decodedJWT)) {
            UserDetails userDetails = accountService.loadUserByUsername(username);

            LOGGER.debug("Loaded UserDetails object: {}", userDetails);

            return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );
        }

        return null;
    }

}
