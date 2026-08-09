package com.example.studymatch.service;

import com.example.studymatch.dto.AuthDto;
import com.example.studymatch.entity.Role;
import com.example.studymatch.entity.User;
import com.example.studymatch.repository.UserRepository;
import com.example.studymatch.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // 회원가입 로직
    public void signup(AuthDto.SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 암호화!
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user); // DB에 저장
    }

    // 로그인 로직
    public String login(AuthDto.LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 입력받은 비밀번호와 DB의 암호화된 비밀번호 비교
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 비밀번호가 맞다면 JWT 토큰 발급
        return jwtProvider.createToken(user.getEmail(), user.getRole().name());
    }
}
