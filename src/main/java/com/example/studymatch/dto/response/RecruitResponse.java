package com.example.studymatch.dto.response;

import lombok.Builder;

@Builder
public record RecruitResponse(
        Long id,
        String title,
        boolean isClosed,
        String authorEmail,
        String content
) {
}
