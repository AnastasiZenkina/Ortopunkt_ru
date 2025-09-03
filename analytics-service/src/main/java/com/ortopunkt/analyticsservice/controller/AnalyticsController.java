package com.ortopunkt.analyticsservice.controller;

import com.ortopunkt.analyticsservice.service.SocialMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ortopunkt.analyticsservice.service.AnalyticsService;

@RestController
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final SocialMetricsService socialMetricsService;

    @GetMapping("/api/analytics/applications")
    public String getApplicationsAnalytics() {
        return analyticsService.getMonthlyApplications();
    }

    @GetMapping("/analytics/social/vk/ads")
    public String getVkAdsStats(@RequestParam String from,
                                @RequestParam String to) {
        return socialMetricsService.getVkAdsStats(from, to);
    }

    @GetMapping("/analytics/social/vk/community")
    public String getVkCommunityStats(@RequestParam String from,
                                      @RequestParam String to) {
        return socialMetricsService.getVkCommunityStats(from, to);
    }

    @GetMapping("/analytics/social/insta")
    public String getInstaStats(@RequestParam String from,
                                @RequestParam String to) {
        return socialMetricsService.getInstaStats(from, to);
    }
}