package com.example.studymatch.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. 클라이언트가 보낸 요청의 헤더에서 'Authorization' 값을 꺼냅니다.
        String header = request.getHeader("Authorization");

        // 2. 토큰이 존재하고, "Bearer "로 시작하는지 확인합니다. (표준 규칙)
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // "Bearer " 뒷부분의 진짜 토큰 문자열만 잘라냅니다.

            // 3. 토큰이 위조되지 않은 진짜 토큰인지 검사합니다.
            if (jwtProvider.validateToken(token)) {

                // 4. 진짜 토큰이라면 안에 있는 이메일과 권한 정보를 꺼냅니다.
                var claims = jwtProvider.getClaims(token);
                String email = claims.getSubject();
                String role = claims.get("role", String.class);

                // 5. Spring Security에게 "이 사람은 인증된 사람이다!"라고 도장을 찍어줍니다.
                var authentication = new UsernamePasswordAuthenticationToken(
                        email, null, Collections.singletonList(new SimpleGrantedAuthority(role))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 6. 검사가 끝났으니 다음 필터나 원래 가려던 Controller로 요청을 넘겨줍니다.
        filterChain.doFilter(request, response);
    }
}