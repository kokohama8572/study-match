package com.example.studymatch.controller;

import com.example.studymatch.dto.request.RecruitRequest;
import com.example.studymatch.dto.response.RecruitResponse;
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

    //  작성
    @PostMapping
    public ResponseEntity<String> createRecruit(@RequestBody RecruitRequest request, Authentication authentication) {
        recruitService.createRecruit(request, authentication.getName());
        return ResponseEntity.ok("게시글이 성공적으로 작성되었습니다.");
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<RecruitResponse>> getAllRecruits() {
        return ResponseEntity.ok(recruitService.getAllRecruits());
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRecruit(@PathVariable Long id, @RequestBody RecruitRequest request, Authentication authentication) {
        recruitService.updateRecruit(id, request, authentication.getName());
        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }

    //  삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecruit(@PathVariable Long id, Authentication authentication) {
        recruitService.deleteRecruit(id, authentication.getName());
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}