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
        } catch (CustomException e) {
            System.out.println("Exception caught in JwtExceptionFilter: " + e.getMessage());
            handleCustomException(response, e.getErrorType());
        } catch (Exception e) {
            System.out.println("Exception caught in JwtExceptionFilter: " + e.getMessage());
            handleGenericException(response, e);
        }
    }

    private void handleCustomException(HttpServletResponse response, ErrorType errorType)
            throws IOException {
        response.setStatus(errorType.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ExceptionDto(errorType.getMessage())));
        response.getWriter().flush();
    }

    private void handleGenericException(HttpServletResponse response, Exception e)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ExceptionDto("Internal Server Error")));
        response.getWriter().flush();
    }
}