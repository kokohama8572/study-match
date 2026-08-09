package com.example.studymatch.repository;

import com.example.studymatch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
//db인테페이스
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail (String email);
}
