package com.ortopunkt.analyticsservice.service;

import com.ortopunkt.dto.response.ApplicationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnalyticsHttpClient {

    private final RestTemplate restTemplate = new RestTemplate();

    // новый метод для получения всех заявок
    public List<ApplicationResponseDto> getAllApplications() {
        return Arrays.asList(
                restTemplate.getForObject(
                        "http://localhost:8082/api/applications",
                        ApplicationResponseDto[].class
                )
        );
    }
}
