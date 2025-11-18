package com.ortopunkt.analyticsservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class VkTokenStorage {

    private final ObjectMapper objectMapper;
    private final File tokenFile = new File("vk_token.json");
    private final ServiceLogger log = new ServiceLogger(getClass(), "ANALYTICS");

    public StoredToken loadTokenFromFile() {
        if (!tokenFile.exists()) return null;
        try {
            var json = objectMapper.readTree(tokenFile);
            return new StoredToken(
                    json.path("access_token").asText(null),
                    json.path("refresh_token").asText(null),
                    json.hasNonNull("expires_at") ? Instant.parse(json.get("expires_at").asText()) : null
            );
        } catch (IOException e) {
            log.error("Ошибка при загрузке токена из файла: " + e.getMessage());
            return null;
        }
    }

    public void saveTokenToFile(String accessToken, String refreshToken, Instant expiresAt) {
        try {
            ObjectNode json = objectMapper.createObjectNode();
            json.put("access_token", accessToken);
            json.put("refresh_token", refreshToken);
            json.put("expires_at", expiresAt.toString());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(tokenFile, json);
        } catch (IOException e) {
            log.error("Ошибка при сохранении токена в файл: " + e.getMessage());
        }
    }

    public record StoredToken(String accessToken, String refreshToken, Instant expiresAt) {}
}