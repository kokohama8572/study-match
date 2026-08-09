package com.example.studymatch.controller;

import com.example.studymatch.dto.RecruitDto;
import com.example.studymatch.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruits")
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitService recruitService;

    // 1. 게시글 작성
    @PostMapping
    public ResponseEntity<String> createRecruit(@RequestBody RecruitDto.Request request, Authentication authentication) {
        // authentication.getName()을 호출하면 4단계 필터에서 넣어둔 '로그인한 사용자의 이메일'이 나옵니다.
        recruitService.createRecruit(request, authentication.getName());
        return ResponseEntity.ok("게시글이 성공적으로 작성되었습니다.");
    }

    // 2. 게시글 전체 조회 (목록 보기)
    @GetMapping
    public ResponseEntity<List<RecruitDto.Response>> getAllRecruits() {
        return ResponseEntity.ok(recruitService.getAllRecruits());
    }

    // 3. 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRecruit(@PathVariable Long id, @RequestBody RecruitDto.Request request, Authentication authentication) {
        recruitService.updateRecruit(id, request, authentication.getName());
        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }

    // 4. 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecruit(@PathVariable Long id, Authentication authentication) {
        recruitService.deleteRecruit(id, authentication.getName());
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}