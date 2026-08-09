package com.example.studymatch.service;

import com.example.studymatch.dto.RecruitDto;
import com.example.studymatch.entity.Recruit;
import com.example.studymatch.entity.User;
import com.example.studymatch.repository.RecruitRepository;
import com.example.studymatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final UserRepository userRepository;

    // 1. 게시글 작성 (Create)
    @Transactional
    public Long createRecruit(RecruitDto.Request request, String email) {
        // JWT 토큰에서 꺼낸 이메일로 DB에서 작성자 정보를 찾습니다.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Recruit recruit = Recruit.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user) // 찾은 유저를 작성자로 지정
                .build();

        return recruitRepository.save(recruit).getId();
    }

    // 2. 게시글 전체 조회 (Read)
    @Transactional(readOnly = true)
    public List<RecruitDto.Response> getAllRecruits() {
        return recruitRepository.findAll().stream()
                .map(recruit -> RecruitDto.Response.builder()
                        .id(recruit.getId())
                        .title(recruit.getTitle())
                        .content(recruit.getContent())
                        .authorEmail(recruit.getUser().getEmail())
                        .isClosed(recruit.isClosed())
                        .build())
                .collect(Collectors.toList());
    }

    // 3. 게시글 수정 (Update)
    @Transactional
    public void updateRecruit(Long id, RecruitDto.Request request, String email) {
        Recruit recruit = recruitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 수정을 요청한 사람과 작성자가 일치하는지 확인
        if (!recruit.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        recruit.update(request.getTitle(), request.getContent());
    }

    // 4. 게시글 삭제 (Delete)
    @Transactional
    public void deleteRecruit(Long id, String email) {
        Recruit recruit = recruitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (!recruit.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        recruitRepository.delete(recruit);
    }
}
