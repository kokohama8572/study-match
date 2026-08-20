package com.example.studymatch.service;

import com.example.studymatch.dto.request.RecruitRequest;
import com.example.studymatch.dto.response.RecruitResponse;
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

    @Transactional
    public Long createRecruit(RecruitRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Recruit recruit = Recruit.builder()
                .title(request.title())
                .content(request.content())
                .user(user)
                .build();

        return recruitRepository.save(recruit).getId();
    }

    @Transactional(readOnly = true)
    public List<RecruitResponse> getAllRecruits() {
        return recruitRepository.findAll().stream()
                .map(recruit -> RecruitResponse.builder()
                        .id(recruit.getId())
                        .title(recruit.getTitle())
                        .content(recruit.getContent())
                        .authorEmail(recruit.getUser().getEmail())
                        .isClosed(recruit.isClosed())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateRecruit(Long id,RecruitRequest request, String email) {
        Recruit recruit = recruitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 수정을 요청한 사람과 작성자가 일치하는지 확인
        if (!recruit.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        recruit.update(request.title(), request.content());
    }

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
