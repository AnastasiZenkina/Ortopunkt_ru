package com.ortopunkt.analyticsservice.client;

import org.springframework.stereotype.Component;

@Component
public class VkAdsClient {

    public String fetchCampaignStats(String from, String to) {
        // TODO: запрос в VK Ads API
        return "VK Ads stats from " + from + " to " + to;
    }
}