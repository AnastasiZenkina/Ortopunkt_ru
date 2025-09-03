package com.ortopunkt.telegram.socialintegration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SocialAnalyticsClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8083/analytics/social"; // порт analytics-service

    public String getVkAdsStats(String from, String to) {
        return restTemplate.getForObject(baseUrl + "/vk/ads?from=" + from + "&to=" + to, String.class);
    }

    public String getVkCommunityStats(String from, String to) {
        return restTemplate.getForObject(baseUrl + "/vk/community?from=" + from + "&to=" + to, String.class);
    }

    public String getInstaStats(String from, String to) {
        return restTemplate.getForObject(baseUrl + "/insta?from=" + from + "&to=" + to, String.class);
    }
}