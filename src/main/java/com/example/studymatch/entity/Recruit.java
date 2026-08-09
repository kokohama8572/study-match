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
    private boolean isClosed; // 모집 마감 여부 (false: 모집중, true: 마감)

    // 여러 개의 게시글(Many)은 하나의 작성자(One)를 가집니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 작성자 정보

    @Builder
    public Recruit(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.isClosed = false; // 게시글을 처음 작성하면 기본으로 '모집 중' 상태
        this.user = user;
    }

    // 💡 누락되었던 게시글 수정용 메서드입니다.
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 모집 마감 처리를 위한 메서드
    public void close() {
        this.isClosed = true;
    }
}