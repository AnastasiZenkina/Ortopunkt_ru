package com.ortopunkt.analyticsservice.client;

import com.ortopunkt.analyticsservice.config.VkAuthClient;
import com.ortopunkt.logging.ServiceLogger;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class VkAdsClient {

    @Value("${vk.ads.v2.url}")
    private String adsV2Url;

    @Value("${vk.ads.v3.url}")
    private String adsV3Url;

    private final RestTemplate restTemplate;
    private final VkAuthClient vkAuthClient;
    private final ServiceLogger log = new ServiceLogger(getClass(), "ANALYTICS");

    @Retry(name = "vkApi")
    @RateLimiter(name = "vkApi")
    public CompletableFuture<String> fetchCampaignsJson(String from, String to) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = UriComponentsBuilder
                        .fromHttpUrl(adsV2Url)
                        .queryParam("date_from", from)
                        .queryParam("date_to", to)
                        .queryParam("metrics", "base,social_network")
                        .toUriString();
                return sendGetRequest(url);
            } catch (Exception e) {
                log.error("Ошибка при получении JSON статистики VK Ads: " + e.getMessage());
                return "{}";
            }
        });
    }

    public String fetchCreativeStatsByPlanIds(List<Long> planIds, String from, String to) {
        try {
            String ids = String.join(",", planIds.stream().map(String::valueOf).toList());
            String url = UriComponentsBuilder
                    .fromHttpUrl(adsV3Url)
                    .queryParam("ids", ids)
                    .queryParam("id_type", "ad_plan_id")
                    .queryParam("date_from", from)
                    .queryParam("date_to", to)
                    .queryParam("fields", "base,social_network")
                    .queryParam("banner_status", "active")
                    .queryParam("ad_group_status", "active")
                    .queryParam("limit", "250")
                    .queryParam("sort_by", "base.clicks")
                    .queryParam("d", "desc")
                    .toUriString();
            return sendGetRequest(url);
        } catch (Exception e) {
            log.error("Ошибка при получении креативов VK Ads: " + e.getMessage());
            return "{}";
        }
    }

    private String sendGetRequest(String url) {
        String token = vkAuthClient.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);
        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        return response.getBody();
    }
}