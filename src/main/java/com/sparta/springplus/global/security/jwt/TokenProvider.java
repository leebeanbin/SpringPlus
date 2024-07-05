package com.sparta.springplus.global.security.jwt;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springplus.domain.user.User;
import com.sparta.springplus.domain.user.UserRefreshToken;
import com.sparta.springplus.domain.user.repository.UserRefreshTokenRepository;
import com.sparta.springplus.domain.user.repository.UserRepository;
import com.sparta.springplus.global.enums.ErrorType;
import com.sparta.springplus.global.enums.UserRole;
import com.sparta.springplus.global.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class TokenProvider {

    // access 토큰 헤더
    public static final String AUTH_ACCESS_HEADER = "AccessToken";
    // refresh 토큰 헤더
    public static final String AUTH_REFRESH_HEADER = "RefreshToken";
    // 사용자 권한
    public static final String AUTHORIZATION_KEY = "auth";
    // 토큰 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    private final String secretKey;
    private final long expirationHours;
    private final long refreshExpirationHours;
    private final long reissueLimit;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private final SignatureAlgorithm SIG = SignatureAlgorithm.HS256;

    private final Key key;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;

    public TokenProvider(
            @Value("${JWT_KEY}") String secretKey,
            @Value("${ACCESS_EXPIRATION}") long expirationHours,
            @Value("${REFRESH_EXPIRATION}") long refreshExpirationHours,
            UserRefreshTokenRepository userRefreshTokenRepository,
            UserRepository userRepository) {
        this.secretKey = secretKey;
        this.expirationHours = expirationHours;
        this.refreshExpirationHours = refreshExpirationHours;
        this.userRefreshTokenRepository = userRefreshTokenRepository;
        reissueLimit = refreshExpirationHours * 60 / expirationHours;

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = hmacShaKeyFor(keyBytes);
        this.userRepository = userRepository;
    }

    public String createAccessToken(String username, UserRole role) {
        return BEARER_PREFIX + Jwts.builder()
                .signWith(key, SIG)
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)))
                .compact();
    }

    public String createRefreshToken(String username, UserRole role) {
        return BEARER_PREFIX + Jwts.builder()
                .signWith(key, SIG)
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(refreshExpirationHours, ChronoUnit.HOURS)))
                .compact();
    }

    public String validateTokenAndGetSubject(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (SignatureException e) {
            throw new CustomException(ErrorType.INVALID_REFRESH_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorType.INVALID_REFRESH_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(ErrorType.INVALID_REFRESH_TOKEN);
        } catch (MalformedJwtException e) {
            throw new CustomException(ErrorType.INVALID_REFRESH_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorType.INVALID_REFRESH_TOKEN);
        } catch (Exception e) {
            throw new CustomException(ErrorType.UNAPPROVED_USER);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    @Transactional(readOnly = true)
    public void validateRefreshToken(String refreshToken, String oldAccessToken)
            throws JsonProcessingException {
        log.info("Validating refresh token: {}", refreshToken);
        getUserInfoFromToken(refreshToken);
        Long userId = Long.parseLong(decodeJwtPayloadSubject(oldAccessToken).split(":")[0]);
        userRefreshTokenRepository.findByIdAndReissueCountLessThan(userId, reissueLimit)
                .filter(RefreshToken -> RefreshToken.validateRefreshToken(refreshToken))
                .orElseThrow(() -> new ExpiredJwtException(null, null, "Refresh token expired."));
        log.info("Refresh token validated for user ID: {}", userId);
    }

    @Transactional
    public String recreateAccessToken(String oldAccessToken) throws JsonProcessingException {
        String subject = decodeJwtPayloadSubject(oldAccessToken);
        Long userId = Long.parseLong(subject);

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("user not found for the given ID: " + userId);
        }
        User user = userOptional.get();

        Optional<UserRefreshToken> userRefreshTokenOptional = userRefreshTokenRepository.findByIdAndReissueCountLessThan(userId, reissueLimit);
        if (userRefreshTokenOptional.isPresent()) {
            UserRefreshToken userRefreshToken = userRefreshTokenOptional.get();
            userRefreshToken.increaseReissueCount();
            userRefreshTokenRepository.save(userRefreshToken);
        } else {
            throw new ExpiredJwtException(null, null, "Refresh token expired or reissue limit exceeded.");
        }

        log.info("Access token reissued for user: {}", user.getUsername());
        return createAccessToken(user.getUsername(), user.getUserRole());
    }

    public boolean existRefreshToken(String refreshToken) throws JsonProcessingException {
        Long userId = Long.parseLong(decodeJwtPayloadSubject(refreshToken));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("user not found"));
        return user.getRefresh();
    }

    private String decodeJwtPayloadSubject(String oldAccessToken) throws JsonProcessingException {
        return objectMapper.readValue(
                new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]),
                        StandardCharsets.UTF_8),
                Map.class
        ).get("sub").toString();
    }

    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTH_ACCESS_HEADER);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER_PREFIX)) {
            return accessToken.substring(BEARER_PREFIX.length());
        }
        return accessToken;
    }

    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        String refreshToken = request.getHeader(AUTH_REFRESH_HEADER);
        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith(BEARER_PREFIX)) {
            return refreshToken.substring(BEARER_PREFIX.length());
        }
        return refreshToken;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
