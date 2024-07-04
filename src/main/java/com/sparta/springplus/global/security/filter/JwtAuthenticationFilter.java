package com.sparta.springplus.global.security.filter;

import static com.sparta.springplus.global.enums.ResponseMessage.SUCCESS_LOGIN;
import static com.sparta.springplus.global.enums.ResponseMessage.SUCCESS_LOGOUT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springplus.domain.common.ApiResponse;
import com.sparta.springplus.domain.user.UserRefreshToken;
import com.sparta.springplus.domain.user.dto.LoginRequestDto;
import com.sparta.springplus.domain.user.repository.UserRefreshTokenRepository;
import com.sparta.springplus.domain.user.repository.UserRepository;
import com.sparta.springplus.global.enums.AuthEnum;
import com.sparta.springplus.global.enums.ErrorType;
import com.sparta.springplus.global.enums.Status;
import com.sparta.springplus.global.enums.UserRole;
import com.sparta.springplus.global.exception.CustomException;
import com.sparta.springplus.global.exception.ExceptionDto;
import com.sparta.springplus.global.security.UserDetailsImpl;
import com.sparta.springplus.global.security.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserRefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(TokenProvider tokenProvider, UserRepository userRepository,
            UserRefreshTokenRepository userRefreshTokenRepository,
            AuthenticationManager authenticationManager,
            JwtAuthenticationEntryPoint authenticationEntryPoint) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.refreshTokenRepository = userRefreshTokenRepository;
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if ("/users/login".equals(requestURI)) {
            handleLogin(request, response);
            return;
        } else if ("/users/logout".equals(requestURI)) {
            handleLogout(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            attemptAuthentication(request, response);
        } catch (AuthenticationException e) {
            unsuccessfulAuthentication(request, response, e);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) {
        try {
            processLogout(request, response);
        } catch (IOException e) {
            log.error("Logout failed", e);
        }
    }

    public void attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        LoginRequestDto requestDto = objectMapper.readValue(request.getInputStream(),
                LoginRequestDto.class);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(),
                        requestDto.getPassword()
                )
        );
        successfulAuthentication(request, response, authentication);
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication auth)
            throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        com.sparta.springplus.domain.user.User user = userDetails.getUser();

        if (Status.DEACTIVATE.equals(user.getStatus())) {
            throw new CustomException(ErrorType.DEACTIVATE_USER);
        }

        String username = userDetails.getUsername();
        UserRole role = user.getUserRole();

        String accessToken = tokenProvider.createAccessToken(username, role);
        String refreshToken = tokenProvider.createRefreshToken(username, role);

        // Logging token creation
        log.info("AccessToken created: {}", accessToken);
        log.info("RefreshToken created: {}", refreshToken);

        user.setRefresh(false);
        userRepository.save(user);

        UserRefreshToken userRefreshToken = refreshTokenRepository.findByUser(user);
        if (userRefreshToken != null) {
            userRefreshToken.updateRefreshToken(refreshToken);
            userRefreshToken.invalidate(false);
        } else {
            refreshTokenRepository.save(new UserRefreshToken(user, refreshToken));
        }

        response.addHeader(AuthEnum.ACCESS_TOKEN.getValue(), accessToken);
        response.addHeader(AuthEnum.REFRESH_TOKEN.getValue(), refreshToken);

        // Construct JSON response
        String jsonResponse = String.format("{\"accessToken\": \"%s\", \"refreshToken\": \"%s\", \"message\": \"%s\"}",
                accessToken, refreshToken, SUCCESS_LOGIN.getMessage());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        ErrorType errorType = ErrorType.NOT_FOUND_AUTHENTICATION_INFO;
        response.setStatus(errorType.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter()
                .write(objectMapper.writeValueAsString(new ExceptionDto(errorType)));
        response.getWriter().flush();
    }

    private void processLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            com.sparta.springplus.domain.user.User user = userDetails.getUser();
            user.setRefresh(true);
            userRepository.save(user);

            UserRefreshToken userRefreshToken = refreshTokenRepository.findByUser(user);
            if (userRefreshToken != null) {
                userRefreshToken.invalidate(true);
                refreshTokenRepository.save(userRefreshToken);
            }
        }

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.success(SUCCESS_LOGOUT.getMessage())));
        response.getWriter().flush();

        // Log logout action
        log.info("User logged out successfully");
    }
}
