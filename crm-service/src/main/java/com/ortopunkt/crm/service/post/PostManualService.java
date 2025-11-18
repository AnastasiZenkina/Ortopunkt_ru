package com.ortopunkt.crm.service.post;

import com.ortopunkt.dto.request.PostRequestDto;
import com.ortopunkt.crm.entity.Post;
import com.ortopunkt.crm.repository.PostRepository;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class PostManualService {

    private final PostRepository postRepository;
    private final ServiceLogger log = new ServiceLogger(getClass(), "CRM");

    @Transactional
    public void saveManual(PostRequestDto dto, String platform) {
        log.info("Сохранение ручных данных для платформы: " + platform);

        Post post = postRepository.findAll().stream()
                .reduce((first, second) -> second)
                .orElseGet(() -> {
                    Post p = new Post();
                    p.setMonthYear(YearMonth.now().toString());
                    p.setPostDate(LocalDate.now());
                    return p;
                });

        if ("vk".equalsIgnoreCase(platform)) {
            PostMapper.applyVkManual(dto, post);
        } else if ("insta".equalsIgnoreCase(platform)) {
            PostMapper.applyInstaManual(dto, post);
        }

        postRepository.save(post);
    }
}