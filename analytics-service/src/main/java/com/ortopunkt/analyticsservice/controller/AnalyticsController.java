package com.ortopunkt.analyticsservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ortopunkt.analyticsservice.service.AnalyticsService;

@RestController
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/api/analytics/applications")
    public String getApplicationsAnalytics() {
        return analyticsService.getMonthlyApplications();
    }
}