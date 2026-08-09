package com.example.studymatch.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final long expirationTime = 1000 * 60 * 60; // 토큰 유효시간 1시간 (밀리초 단위)

    // application.yml에 작성한 비밀키를 가져와서 암호화 키로 변환합니다.
    public JwtProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 로그인 성공 시 JWT 토큰을 만들어주는 메서드
    public String createToken(String email, String role) {
        return Jwts.builder()
                .subject(email) // 토큰의 주인 (사용자 이메일)
                .claim("role", role) // 토큰에 담을 추가 정보 (권한)
                .issuedAt(new Date()) // 발행 시간
                .expiration(new Date(System.currentTimeMillis() + expirationTime)) // 만료 시간
                .signWith(secretKey) // 서버의 비밀키로 서명 (위조 방지)
                .compact();
    }

    // 1. 토큰이 위조되지 않았고, 만료되지 않았는지 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // 토큰이 조작되었거나 시간이 만료되면 에러가 발생하여 false를 반환합니다.
            return false;
        }
    }

    // 2. 유효한 토큰 안에서 사용자의 이메일과 권한(Role) 정보를 꺼내는 메서드
    public io.jsonwebtoken.Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}