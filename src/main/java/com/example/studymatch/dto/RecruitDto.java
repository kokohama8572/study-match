package com.example.studymatch.dto;

import lombok.Builder;
import lombok.Getter;

public class RecruitDto {

    // 클라이언트 -> 서버 (게시글 작성/수정 요청)
    @Getter
    public static class Request {
        private String title;
        private String content;
    }

    // 서버 -> 클라이언트 (게시글 정보 응답)
    @Getter
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private String authorEmail; // 작성자 이메일
        private boolean isClosed;   // 모집 마감 여부
    }
}