package com.sparta.springplus.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springplus.domain.common.ApiResponse;
import com.sparta.springplus.global.enums.ErrorType;
import com.sparta.springplus.global.exception.ExceptionDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HandlerExceptionResolver resolver;

    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        ErrorType errorType = determineErrorType(authException);

        response.setStatus(errorType.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ExceptionDto exceptionDto = new ExceptionDto(errorType);
        String jsonResponse = objectMapper.writeValueAsString(ApiResponse.error(exceptionDto.getMessage()));
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();

        log.error("Unauthorized error: {}", authException.getMessage());
    }

    private ErrorType determineErrorType(AuthenticationException authException) {
        Throwable cause = authException.getCause();

        if (cause instanceof SignatureException) {
            return ErrorType.INVALID_REFRESH_TOKEN;
        } else if (cause instanceof ExpiredJwtException) {
            return ErrorType.INVALID_REFRESH_TOKEN;
        } else {
            return ErrorType.UNAPPROVED_USER;
        }
    }
}