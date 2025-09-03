package com.ortopunkt.analyticsservice.service;

import com.ortopunkt.analyticsservice.client.InstaGraphClient;
import com.ortopunkt.analyticsservice.client.VkAdsClient;
import com.ortopunkt.analyticsservice.client.VkCommunityClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialMetricsService {

    private final VkAdsClient vkAdsClient;
    private final VkCommunityClient vkCommunityClient;
    private final InstaGraphClient instaGraphClient;

    public String getVkAdsStats(String from, String to) {
        return vkAdsClient.fetchCampaignStats(from, to);
    }

    public String getVkCommunityStats(String from, String to) {
        return vkCommunityClient.fetchGroupStats(from, to);
    }

    public String getInstaStats(String from, String to) {
        return instaGraphClient.fetchInstaStats(from, to);
    }
}