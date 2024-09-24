package com.sparta.outsourcing.jwt;

import com.sparta.outsourcing.domain.user.dto.CustomUserDetails;
import com.sparta.outsourcing.domain.user.entity.Role;
import com.sparta.outsourcing.domain.user.entity.TokenType;
import com.sparta.outsourcing.domain.user.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(TokenType tokenType, CustomUserDetails user) {
        long currentTimeMillis = System.currentTimeMillis();
        Date issuedAt = new Date(currentTimeMillis); // 발급일
        Date expirationDate = new Date(currentTimeMillis + tokenType.getExpireMs()); // 만료일


        String token = Jwts.builder()
                .claim("category", tokenType.name().toLowerCase())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .setExpiration(expirationDate) // 만료 시간
                .setIssuedAt(issuedAt) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();

        return TokenType.ACCESS.equals(tokenType) ? BEARER_PREFIX + token : token;
    }

    public String getCategory(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("category", String.class);
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("email", String.class);
    }

    // JWT 를 HTTP 헤더에 추가
    public void addJwtToHeader(String token, HttpServletResponse res) {
        res.setHeader(AUTHORIZATION_HEADER, token);
    }

    // JWT 토큰 substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length()).trim();
        }
        throw new IllegalArgumentException("Not Found Token");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public CustomUserDetails getCustomUserDetailsFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

        return CustomUserDetails.builder()
                .email(email)
                .role(Role.valueOf(role))
                .build();
    }

    public String getTokenFromRequest(HttpServletRequest req) {
        String token = req.getHeader(AUTHORIZATION_HEADER);
        if (token != null) {
            try {
                return URLDecoder.decode(token, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return null;
    }

    public Boolean isExpired(String token) { // TODO: 에러를 던질지 false 를 반환할지
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }
}
