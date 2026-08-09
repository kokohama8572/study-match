package com.example.studymatch.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recruits")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 모집글 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 모집글 내용

    @Column(nullable = false)
    private boolean isClosed; // 모집 마감


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Recruit(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.isClosed = false; // 기본으로모집 중 상태
        this.user = user;
    }

    //수정용 메서드
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 모집 마감 처리
    public void close() {
        this.isClosed = true;
    }
}