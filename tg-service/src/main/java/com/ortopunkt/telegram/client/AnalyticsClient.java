package com.ortopunkt.telegram.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AnalyticsClient {

    private final RestTemplate restTemplate;
    private final String analyticsUrl;

    public AnalyticsClient(RestTemplate restTemplate,
                           @Value("${analytics.url}") String analyticsUrl) {
        this.restTemplate = restTemplate;
        this.analyticsUrl = analyticsUrl;
    }

    public String getVkCommunityInfo() {
        return restTemplate.getForObject(
                analyticsUrl + "/analytics/social/vk/community",
                String.class
        );
    }

    public String getVkAdsInfo() {
        return restTemplate.getForObject(
                analyticsUrl + "/analytics/social/vk/ads",
                String.class
        );
    }

    public String getInstaCommunityInfo() {
        return restTemplate.getForObject(
                analyticsUrl + "/analytics/social/insta/community",
                String.class
        );
    }
}