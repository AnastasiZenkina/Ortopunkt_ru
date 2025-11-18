package com.ortopunkt.analyticsservice.client;

import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnalyticsClient {

    @Value("${crm.api.url}")
    private String crmApiUrl;

    private final RestTemplate restTemplate;
    private final ServiceLogger log = new ServiceLogger(getClass(), "ANALYTICS");

    public List<ApplicationResponseDto> getAllApplications() {
        try {
            ApplicationResponseDto[] apps = restTemplate.getForObject(
                    crmApiUrl + "/api/applications",
                    ApplicationResponseDto[].class
            );
            return Arrays.asList(apps != null ? apps : new ApplicationResponseDto[0]);
        } catch (Exception e) {
            log.error("Error fetching applications: " + e.getMessage());
            return List.of();
        }
    }
}