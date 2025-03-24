package me.dev.demo.config;
import jakarta.servlet.FilterChain;
import org.springframework.security.core.Authentication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // CSRF 보안 비활성화
            .authorizeHttpRequests((auth) -> auth
                .anyRequest().permitAll() // 모든 요청을 인증 없이 허용
            );
        return http.build();
    }
}