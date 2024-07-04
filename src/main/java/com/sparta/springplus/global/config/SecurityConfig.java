package com.sparta.springplus.global.config;

import com.sparta.springplus.global.security.filter.JwtAuthenticationEntryPoint;
import com.sparta.springplus.global.security.filter.JwtAuthenticationFilter;
import com.sparta.springplus.global.security.filter.JwtAuthorizationFilter;
import com.sparta.springplus.global.security.filter.JwtExceptionFilter;
import com.sparta.springplus.global.security.jwt.TokenProvider;
import com.sparta.springplus.domain.user.repository.UserRefreshTokenRepository;
import com.sparta.springplus.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 지원을 가능하게 함
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint entryPoint;
    private final UserRepository userRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final AuthenticationConfiguration authenticationConfiguration;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(tokenProvider, userRepository,
                userRefreshTokenRepository, authenticationManager(authenticationConfiguration),
                entryPoint);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(tokenProvider,userRepository);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(HttpMethod.POST, "/users").permitAll()
                                .requestMatchers("/email", "/email/**")
                                .permitAll()    // requestMatchers의 인자로 전달된 url은 모두에게 허용
                                .requestMatchers(HttpMethod.GET, "/feed/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/{userId}/follower")
                                .permitAll() // 팔로워 조회
                                .requestMatchers(HttpMethod.GET, "/users/{userId}/following")
                                .permitAll() // 팔로잉 조회
                                .requestMatchers(HttpMethod.GET, "/feed/{{feedId}}/reply")
                                .permitAll() // 댓글 조회
                                .anyRequest().authenticated()    // 그 외의 모든 요청은 인증 필요
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );   // 세션을 사용하지 않으므로 STATELESS 설정

        // 인증 되지않은 유저 요청 시 동작
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(entryPoint));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter(), JwtAuthorizationFilter.class);

        http.logout(auth -> auth
                .logoutUrl("/users/logout")
                .addLogoutHandler(new SecurityContextLogoutHandler())
                .logoutSuccessHandler(
                        (((request, response, authentication) -> SecurityContextHolder.clearContext()))));

        return http.build();
    }
}
