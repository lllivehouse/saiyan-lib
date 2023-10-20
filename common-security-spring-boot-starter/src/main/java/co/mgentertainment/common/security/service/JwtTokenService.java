package co.mgentertainment.common.security.service;

import co.mgentertainment.common.security.config.SecureProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author larry
 * @createTime 2022/12/5
 * @description JwtTokenService
 */
@Slf4j
@RequiredArgsConstructor
public class JwtTokenService {

    private static final String TOKEN_HEADER_NAME = "Authorization";
    private static final String SIGN_HEADER_NAME = "Sign";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final SecureProperties secureProperties;

    public String getAccessTokenFromRequest(HttpServletRequest request) {
        return getTokenFromRequest(request, TOKEN_HEADER_NAME);
    }

    public String getRefreshTokenFromRequest(HttpServletRequest request) {
        return getTokenFromRequest(request, SIGN_HEADER_NAME);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String generateToken(String subject) {
        Map<String, Object> payload = new HashMap<>(1);
        payload.put("type", TokenType.ACCESS.getCode());
        return buildToken(payload, subject, secureProperties.getTokenTtl() * 1000);
    }

    public String generateRefreshToken(String subject) {
        Map<String, Object> payload = new HashMap<>(1);
        payload.put("type", TokenType.REFRESH.getCode());
        return buildToken(payload, subject, secureProperties.getRefreshTokenTtl() * 1000);
    }

    private String buildToken(Map<String, Object> payload, String subject, long expiration) {
        Map<String, Object> headers = new HashMap<>(2);
        headers.put("alg", "SHA-512");
        headers.put("typ", "JWT");
        return Jwts.builder()
                .setHeader(headers)
                .setClaims(payload)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secureProperties.getJwtSecret())
                .compact();
    }

    public boolean verifyToken(TokenType type, String token, String subject) {
        boolean signed = Jwts.parser().isSigned(token);
        if (!signed) {
            return false;
        }
        String username = extractUsername(token);
        return type == extractTokenType(token) && StringUtils.equals(username, subject);
    }

    private boolean isTokenExpired(String token) {
        Date expiredDate = extractExpiration(token);
        return expiredDate == null || expiredDate.before(new Date());
    }

    private String getTokenFromRequest(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        if (!Strings.hasText(token)) {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return null;
            }
            token = Arrays.stream(cookies)
                    .filter(cookie -> headerName.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return token != null && StringUtils.startsWith(token, TOKEN_PREFIX) ? token.substring(TOKEN_PREFIX.length()) : null;
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private TokenType extractTokenType(String token) {
        return TokenType.getByCode(extractAllClaims(token).get("type", Integer.class));
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secureProperties.getJwtSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.warn("fail to parse jwt", e);
            return new DefaultClaims();
        }
    }

    public enum TokenType {
        ACCESS(0),
        REFRESH(1);

        private int code;

        TokenType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public static TokenType getByCode(Integer code) {
            if (code == null) {
                return null;
            }
            return Arrays.stream(TokenType.values()).filter(e -> e.getCode() == code).findFirst().orElse(null);
        }
    }
}