package com.ortopunkt.analyticsservice.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortopunkt.logging.GlobalExceptionHandler;
import com.ortopunkt.logging.ServiceLogger;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class VkAuthClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final VkTokenStorage tokenStorage;
    private final ServiceLogger log = new ServiceLogger(getClass(), "ANALYTICS");

    @Value("${vk.ads.client.id}")
    private String clientId;

    @Value("${vk.ads.client.secret}")
    private String clientSecret;

    @Value("${vk.ads.refresh.token:}")
    private String refreshToken;

    private volatile String accessToken;
    private volatile Instant expiresAt;

    @PostConstruct
    public void init() {
        var loaded = tokenStorage.loadTokenFromFile();
        if (loaded != null) {
            this.accessToken = loaded.accessToken();
            this.refreshToken = loaded.refreshToken();
            this.expiresAt = loaded.expiresAt();
            log.info("VK токен загружен из файла, срок действия до " + expiresAt);
        }
        if (isExpiredSoon()) refreshAccessToken();
    }

    public synchronized String getAccessToken() {
        if (accessToken == null || isExpiredSoon()) refreshAccessToken();
        return accessToken;
    }

    private boolean isExpiredSoon() {
        return expiresAt == null || Instant.now().isAfter(expiresAt.minusSeconds(600));
    }

    @Retry(name = "vkApi")
    @RateLimiter(name = "vkApi")
    public void refreshAccessToken() {
        try {
            String response = restTemplate.postForObject(
                    "https://ads.vk.com/api/v2/oauth2/token.json",
                    new HttpEntity<>(buildForm(), buildHeaders()),
                    String.class
            );

            JsonNode json = objectMapper.readTree(response);
            if (!json.hasNonNull("access_token")) {
                log.error("VK token refresh failed: " + response);
                return;
            }

            this.accessToken = json.get("access_token").asText();
            long ttl = json.hasNonNull("expires_in") ? json.get("expires_in").asLong() : 86400L;
            this.expiresAt = Instant.now().plusSeconds(Math.max(60, ttl));
            if (json.hasNonNull("refresh_token")) this.refreshToken = json.get("refresh_token").asText();

            tokenStorage.saveTokenToFile(accessToken, refreshToken, expiresAt);
            log.info("VK токен успешно обновлён, срок действия до " + expiresAt);

        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
            log.error("Ошибка при обновлении токена VK: " + e.getMessage());
        }
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(clientId, clientSecret);
        return headers;
    }

    private MultiValueMap<String, String> buildForm() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        if (refreshToken != null && !refreshToken.isBlank()) {
            form.add("grant_type", "refresh_token");
            form.add("refresh_token", refreshToken);
        } else {
            form.add("grant_type", "client_credentials");
        }
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        return form;
    }
}