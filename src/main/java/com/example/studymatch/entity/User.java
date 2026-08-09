package com.example.studymatch.entity;

import jakarta.persistence.*;
import lombok.*;
//회원가입하고 로그인 정보를 담는 엔티티임
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false , unique = true)
    private String email;

    @Column (nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)

    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String email, String password, Role role){
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
