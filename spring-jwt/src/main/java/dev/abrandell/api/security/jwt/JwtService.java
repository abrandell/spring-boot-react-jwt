package dev.abrandell.api.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * Service for general JWT methods.
 */
@Service
public class JwtService implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire-time}")
    private int expireTime;

    @Value("${jwt.cookie-name}")
    private String jwtCookieName;

    @Value("${jwt.header-csrf-name}")
    private String headerCsrfName;

    @Value("${jwt.csrf-claim-name}")
    private String csrfClaimName;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    private Algorithm algorithm;

    @Override
    public void afterPropertiesSet() {
        this.algorithm = Algorithm.HMAC512(secret.getBytes());
    }

    String generateToken(@NonNull Authentication auth, @NonNull final String csrfToken) {
        Assert.isTrue(auth.isAuthenticated(), "Authentication object is not authenticated!");

        return JWT.create()
            .withSubject(auth.getName())
            .withIssuer(issuer)
            .withAudience(audience)
            .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
            .withClaim(csrfClaimName, csrfToken)
            .sign(algorithm);
    }

    /**
     * Decodes and verifies the JWT from a {@link Cookie}.
     * @param cookie must not be null.
     * @return Decoded JWT in the form of {@link DecodedJWT}
     */
    DecodedJWT decodeAndVerifyJWT(final Cookie cookie) {
        return JWT.require(algorithm)
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
            .verify(cookie.getValue());
    }

    Cookie createJwtCookie(final String jwt) {
        Cookie cookie = new Cookie(jwtCookieName, jwt);
        cookie.setHttpOnly(true);
        return cookie;
    }

    boolean csrfHeaderMatchesJwtClaim(String headerValue, DecodedJWT jwt) {
        final String jwtClaim = jwt.getClaim("CSRF").asString();
        LOGGER.debug("Header CSRF: {} \n JWT CSRF-CLAIM: {}", headerValue, jwtClaim);
        return headerValue.equals(jwtClaim);
    }

    /**
     * Fetches the header with the name "CSRF" if it exists.
     * @param request must not be null.
     * @return Optional of CSRF token in header. If not found, returns null.
     */
    Optional<String> fetchCsrfHeader(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(headerCsrfName));
    }

    Optional<Cookie> fetchJwtCookie(Cookie... cookies) {
        if (cookies.length == 0) {
            return Optional.empty();
        } else {
            return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(jwtCookieName))
                .findFirst();
        }
    }

}
