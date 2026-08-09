package com.example.studymatch.config;

import com.example.studymatch.security.JwtAuthenticationFilter;
import com.example.studymatch.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor // ⭐️ @RequiredArgsConstructor를 꼭 추가해 주세요!
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. REST API이므로 CSRF 보안은 끄고, 세션도 사용하지 않도록(STATELESS) 설정합니다.
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 2. URL별 접근 권한을 설정합니다.
// 2. URL별 접근 권한을 설정합니다.
                .authorizeHttpRequests(auth -> auth
                        // ⭐️ 아래 줄에 "/error" 가 확실하게 추가되어 있어야 합니다!
                        .requestMatchers("/api/auth/**", "/error").permitAll()
                        .anyRequest().authenticated()
                )

                // 3. 방금 만든 JwtAuthenticationFilter를 기존 아이디/비밀번호 필터 앞에 끼워 넣습니다.
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}