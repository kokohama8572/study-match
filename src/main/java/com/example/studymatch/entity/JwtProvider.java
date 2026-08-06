package com.example.studymatch.entity;

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
}
