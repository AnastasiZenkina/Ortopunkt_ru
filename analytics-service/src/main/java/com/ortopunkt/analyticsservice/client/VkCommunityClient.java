package com.ortopunkt.analyticsservice.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortopunkt.dto.response.PostResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class VkCommunityClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ServiceLogger log = new ServiceLogger(getClass(), "ANALYTICS");

    @Value("${vk.access.token}")
    private String accessToken;

    @Value("${vk.api.version}")
    private String apiVersion;

    @Value("${vk.group.id}")
    private String groupId;

    @Value("${crm.service.url:http://localhost:8082}")
    private String crmServiceUrl;

    public Long fetchMembersCount() {
        try {
            String url = "https://api.vk.com/method/groups.getById"
                    + "?group_id=" + groupId
                    + "&fields=members_count"
                    + "&access_token=" + accessToken
                    + "&v=" + apiVersion;

            JsonNode root = objectMapper.readTree(
                    restTemplate.getForObject(url, String.class)
            );

            JsonNode groups = root.path("response").path("groups");
            if (groups.isArray() && !groups.isEmpty()) {
                return groups.get(0).path("members_count").asLong(0);
            } else {
                log.warn("Некорректный ответ VK API: " + root);
                return 0L;
            }

        } catch (Exception e) {
            log.error("Ошибка при получении числа подписчиков: " + e.getMessage());
            return 0L;
        }
    }

    public JsonNode fetchPosts(int offset, int count) {
        try {
            String url = "https://api.vk.com/method/wall.get"
                    + "?owner_id=-" + groupId
                    + "&count=" + count
                    + "&offset=" + offset
                    + "&filter=owner"
                    + "&access_token=" + accessToken
                    + "&v=" + apiVersion;

            return objectMapper.readTree(
                    restTemplate.getForObject(url, String.class)
            ).path("response").path("items");

        } catch (Exception e) {
            log.error("Ошибка при получении постов offset=" + offset + " count=" + count + ": " + e.getMessage());
            return objectMapper.createArrayNode();
        }
    }

    public PostResponseDto fetchLatestPostFromCrm() {
        try {
            return restTemplate.getForObject(
                    crmServiceUrl + "/api/posts/latest",
                    PostResponseDto.class
            );
        } catch (Exception e) {
            log.error("Ошибка при получении последнего поста из CRM: " + e.getMessage());
            return null;
        }
    }
}