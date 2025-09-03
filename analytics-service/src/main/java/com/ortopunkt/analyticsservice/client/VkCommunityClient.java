package com.ortopunkt.analyticsservice.client;

import org.springframework.stereotype.Component;

@Component
public class VkCommunityClient {

    public String fetchGroupStats(String from, String to) {
        // TODO: запрос в VK Group API
        return "VK Community stats from " + from + " to " + to;
    }
}