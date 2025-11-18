package com.ortopunkt.analyticsservice.controller;

import com.ortopunkt.analyticsservice.service.smm.VkCommunityService;
import com.ortopunkt.analyticsservice.service.crm.AnalyticsService;
import com.ortopunkt.analyticsservice.service.smm.InstaCommunityService;
import com.ortopunkt.analyticsservice.service.ads.VkAdsService;
import com.ortopunkt.dto.request.PostRequestDto;
import com.ortopunkt.dto.response.CampaignResponseDto;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final VkCommunityService vkCommunityService;
    private final InstaCommunityService instaCommunityService;
    private final VkAdsService vkAdsService;
    private final ServiceLogger log = new ServiceLogger(getClass(), "ANALYTICS");

    @GetMapping("/api/analytics/applications")
    public String getApplicationsAnalytics() {
        log.info("Запрос аналитики заявок за месяц");
        return analyticsService.getMonthlyApplications();
    }

    @PostMapping("/api/analytics/applications/update")
    public void updateApplicationFromCrm(@RequestBody ApplicationResponseDto dto) {
        log.info("Получено обновление из CRM по заявке " + dto.getId());
        analyticsService.updateFromCrm(dto);
    }

    @GetMapping("/analytics/social/vk/community")
    public PostRequestDto getVkCommunityStats() {
        log.info("Запрос статистики сообщества VK");
        return vkCommunityService.buildVkReport();
    }

    @GetMapping("/analytics/social/insta/community")
    public PostRequestDto getInstaCommunityStats() {
        log.info("Запрос статистики сообщества Instagram");
        return instaCommunityService.buildInstaReport();
    }

    @GetMapping("/analytics/social/vk/ads")
    public List<CampaignResponseDto> getVkAdsStats() {
        log.info("Запрос отчёта по рекламе VK за прошлый месяц");
        return vkAdsService.getLastMonthStats();
    }
}