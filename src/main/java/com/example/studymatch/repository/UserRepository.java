package com.example.studymatch.repository;

import com.example.studymatch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
//(DB 인터페이스)
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // 로그인: 이메일로 유저 찾기
    boolean existsByEmail (String email);      // 회원가입: 중복 이메일 체크
}
