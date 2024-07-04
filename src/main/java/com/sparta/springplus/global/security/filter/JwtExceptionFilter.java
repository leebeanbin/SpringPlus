package com.sparta.springplus.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.sparta.springplus.global.enums.ErrorType;
import com.sparta.springplus.global.exception.CustomException;
import com.sparta.springplus.global.exception.ExceptionDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response); // JwtAuthenticationFilter 실행
            // TODO : Exception 잡는 것 고치기
        } catch (Exception e) {
            CustomException CustomException = (CustomException) e;
            hadleAuthenticationException(response, CustomException.getErrorType());
        }
    }

    private void hadleAuthenticationException(HttpServletResponse response, ErrorType errorType)
            throws IOException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ExceptionDto(errorType.getMessage())));
        response.getWriter().flush();
    }
}