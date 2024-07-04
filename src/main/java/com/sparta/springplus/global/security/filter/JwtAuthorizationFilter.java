package com.sparta.springplus.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springplus.domain.common.ApiResponse;
import com.sparta.springplus.domain.user.repository.UserRepository;
import com.sparta.springplus.global.enums.AuthEnum;
import com.sparta.springplus.global.enums.ErrorType;
import com.sparta.springplus.global.enums.UserRole;
import com.sparta.springplus.global.exception.ExceptionDto;
import com.sparta.springplus.global.security.UserDetailsImpl;
import com.sparta.springplus.global.security.UserDetailsServiceImpl;
import com.sparta.springplus.global.security.jwt.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();

        if (url.startsWith("/email") || ((url.startsWith("/users") && "POST".equalsIgnoreCase(
                request.getMethod())))) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String accessToken = tokenProvider.getAccessTokenFromHeader(request);
            if (accessToken != null && tokenProvider.validateToken(accessToken)) {
                // token loading -> token 기반으로 Userdetails 생성, token으로 DB에 내가 실제 가진 유저인지 확인
                UserDetailsImpl user = parseUserSpecification(accessToken);
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new ExpiredJwtException(null, null, "Access token expired");
            }
        } catch (ExpiredJwtException e) {
            reissueAccessToken(request, response, e);
            return;
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request, response);
    }

    private UserDetailsImpl parseUserSpecification(String token) {
        try {
            // JWT 토큰에서 주체(subject)를 추출합니다.
            String subject = tokenProvider.validateTokenAndGetSubject(token);

            // JWT 토큰에서 역할(claim)을 추출합니다.
            Claims claims = tokenProvider.getUserInfoFromToken(token);
            String role = claims.get(AuthEnum.AUTHORIZATION_KEY.getValue(), String.class);

            UserDetailsImpl user =  new UserDetailsServiceImpl(userRepository).loadUserByUsername(subject);

            return user;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse user specification from token", e);
        }
    }



    private void reissueAccessToken(HttpServletRequest request, HttpServletResponse response,
            Exception exception) throws IOException {
        try {
            String refreshToken = tokenProvider.getRefreshTokenFromHeader(request);
            if (refreshToken == null) {
                throw exception;
            }
            String oldAccessToken = tokenProvider.getAccessTokenFromHeader(request);
            tokenProvider.validateRefreshToken(refreshToken, oldAccessToken);

            String newAccessToken = tokenProvider.recreateAccessToken(oldAccessToken);
            UserDetailsImpl user = parseUserSpecification(newAccessToken);
            AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(
                    user, newAccessToken, user.getAuthorities());
            authenticated.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticated);

            response.setHeader(AuthEnum.ACCESS_TOKEN.getValue(), "Bearer " + newAccessToken);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(
                    ApiResponse.success("Access token reissued successfully")));
            response.getWriter().flush();

            log.info("Access token reissued: {}", newAccessToken);
        } catch (Exception e) {
            request.setAttribute("exception", e);
            ErrorType errorType = ErrorType.INVALID_REFRESH_TOKEN;
            response.setStatus(errorType.getHttpStatus().value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(
                    new ExceptionDto(errorType.getMessage())));
            response.getWriter().flush();
        }
    }
}
