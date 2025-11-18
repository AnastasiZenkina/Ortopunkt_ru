package com.ortopunkt.analyticsservice.service.ads;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortopunkt.dto.response.CampaignResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class VkAdsParser {

    private final ObjectMapper objectMapper;
    private final ServiceLogger log = new ServiceLogger(getClass(), "ANALYTICS");

    public List<CampaignResponseDto> parseCampaigns(String json) {
        try {
            JsonNode items = objectMapper.readTree(json).path("items");
            if (!items.isArray() || items.isEmpty()) {
                log.warn("Пустой список кампаний в ответе VK Ads");
                return List.of();
            }

            List<CampaignResponseDto> result = new ArrayList<>();
            for (JsonNode node : items) {
                CampaignResponseDto dto = new CampaignResponseDto();
                dto.setId(node.path("id").asLong(0));
                dto.setName(node.path("name").asText(null));
                dto.setReach(node.path("total").path("base").path("shows").asLong(0));
                dto.setImpressions(node.path("total").path("base").path("clicks").asLong(0));
                dto.setSubscribers(node.path("total").path("social_network").path("result_join").asLong(0));
                dto.setMessages(node.path("total").path("social_network").path("result_message").asLong(0));
                dto.setTotalSpent(node.path("total").path("base").path("spent").asDouble(0));
                result.add(dto);
            }
            return result;
        } catch (Exception e) {
            log.error("Ошибка при парсинге кампаний VK Ads: " + e.getMessage());
            return List.of();
        }
    }

    public Map<String, Map<String, Number>> parseCreatives(String json) {
        LinkedHashMap<String, Number> ctrMap = new LinkedHashMap<>();
        LinkedHashMap<String, Number> subsMap = new LinkedHashMap<>();
        final int LIMIT = 10;

        try {
            JsonNode items = objectMapper.readTree(json).path("items");
            if (!items.isArray() || items.isEmpty()) {
                log.warn("Пустой список креативов в ответе VK Ads");
                return Map.of("ctr", ctrMap, "subs", subsMap);
            }

            for (JsonNode item : items) {
                parseCreativeItem(item, ctrMap, subsMap);
            }

            List<String> topIds = ctrMap.keySet().stream()
                    .sorted((a, b) -> {
                        int diffSubs = Integer.compare(
                                subsMap.getOrDefault(b, 0).intValue(),
                                subsMap.getOrDefault(a, 0).intValue());
                        return diffSubs != 0 ? diffSubs :
                                Double.compare(ctrMap.getOrDefault(b, 0d).doubleValue(),
                                        ctrMap.getOrDefault(a, 0d).doubleValue());
                    })
                    .limit(LIMIT)
                    .toList();

            LinkedHashMap<String, Number> topCtr = new LinkedHashMap<>();
            LinkedHashMap<String, Number> topSubs = new LinkedHashMap<>();
            for (String id : topIds) {
                topCtr.put(id, ctrMap.getOrDefault(id, 0d));
                topSubs.put(id, subsMap.getOrDefault(id, 0));
            }

            log.info("Спарсено креативов: " + topCtr.size());
            return Map.of("ctr", topCtr, "subs", topSubs);

        } catch (Exception e) {
            log.error("Ошибка при парсинге креативов VK Ads: " + e.getMessage());
            return Map.of("ctr", ctrMap, "subs", subsMap);
        }
    }

    private void parseCreativeItem(JsonNode item, Map<String, Number> ctrMap, Map<String, Number> subsMap) {
        String creativeId = item.path("id").asText(null);
        if (creativeId != null && !creativeId.isEmpty()) {
            double ctr = item.path("total").path("base").path("ctr").asDouble(0);
            int subs = item.path("total").path("social_network").path("vk_join").asInt(0);
            ctrMap.put(creativeId, ctr);
            subsMap.put(creativeId, subs);
        } else {
            for (JsonNode row : item.path("rows")) {
                String id = row.path("breakdown_value").asText(null);
                if (id == null || id.isEmpty()) continue;
                double ctr = row.path("base").path("ctr").asDouble(0);
                int subs = row.path("social_network").path("vk_join").asInt(0);
                ctrMap.put(id, ctr);
                subsMap.put(id, subs);
            }
        }
    }
}