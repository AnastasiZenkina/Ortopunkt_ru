package com.ortopunkt.telegram.integration.social;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ortopunkt.dto.response.PostResponseDto;
import com.ortopunkt.dto.response.CampaignResponseDto;
import com.ortopunkt.logging.GlobalExceptionHandler;
import com.ortopunkt.telegram.client.AnalyticsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AnalyticsClient analyticsClient;
    private final RestTemplate restTemplate;

    @Value("${analytics.url}")
    private String analyticsUrl;

    public String buildDoctorReport() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String rawVk = analyticsClient.getVkCommunityInfo();
            PostResponseDto vkDto = mapper.readValue(rawVk, PostResponseDto.class);

            String rawInsta = analyticsClient.getInstaCommunityInfo();
            PostResponseDto instaDto = mapper.readValue(rawInsta, PostResponseDto.class);

            String rawCampaign = analyticsClient.getVkAdsInfo();
            List<CampaignResponseDto> campaignList =
                    mapper.readValue(rawCampaign, new TypeReference<List<CampaignResponseDto>>() {});
            CampaignResponseDto campaignDto = campaignList.isEmpty() ? null : campaignList.get(0);

            return DoctorReportFormatter.format(vkDto, instaDto, campaignDto);

        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
            return "Ошибка при формировании отчета доктора.";
        }
    }

    public String buildSmmReport() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String rawVk = analyticsClient.getVkCommunityInfo();
            PostResponseDto vkDto = mapper.readValue(rawVk, PostResponseDto.class);

            String rawInsta = analyticsClient.getInstaCommunityInfo();
            PostResponseDto instaDto = mapper.readValue(rawInsta, PostResponseDto.class);

            return SmmReportFormatter.format(vkDto, instaDto);

        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
            return "Ошибка при формировании SMM-отчета.";
        }
    }

    public String buildTargetReport() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String rawCampaign = analyticsClient.getVkAdsInfo();
            List<CampaignResponseDto> campaignList =
                    mapper.readValue(rawCampaign, new TypeReference<List<CampaignResponseDto>>() {});
            CampaignResponseDto campaignDto = campaignList.isEmpty() ? null : campaignList.get(0);

            return TargetReportFormatter.format(campaignDto);

        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
            return "Ошибка при формировании таргет-отчета.";
        }
    }

    public String buildPatientList() {
        try {
            return restTemplate.getForObject(
                    analyticsUrl + "/api/analytics/applications",
                    String.class
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
            return "Ошибка: не удалось получить данные аналитики.";
        }
    }
}