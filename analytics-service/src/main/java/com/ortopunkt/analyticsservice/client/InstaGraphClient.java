package com.ortopunkt.analyticsservice.client;

import org.springframework.stereotype.Component;

@Component
public class InstaGraphClient {

    public String fetchInstaStats(String from, String to) {
        // TODO: запрос в Instagram Graph API
        return "Instagram stats from " + from + " to " + to;
    }
}