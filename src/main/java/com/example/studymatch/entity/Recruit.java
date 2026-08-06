package com.example.studymatch.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//스터디나 프로젝트 팀원을 구하는 게시글 엔티티 User와 다대일 관계를 가짐 (한 명의 회원이 여러 개의 게시글을 작성할 수 있음)
@Entity
@Table(name = "recruit" )
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruit {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false , columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean isClosed;

    @ManyToOne(fetch = FetchType.LAZY)
    // 1:다수 (필요할 때까지 회원 정보를 DB에서 가져오는 것을 미루겠다)
    @JoinColumn(name = "user_id")
    //데이터베이스 관점에서 외래 키를 설정하는 부분
    private User user;

    @Builder
    public Recruit (String title, String content, User user){
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void close(){
        this.isClosed = true;
    }

}
