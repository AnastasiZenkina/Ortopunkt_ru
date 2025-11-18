package com.ortopunkt.telegram.ui.button.handler.manual;

import com.ortopunkt.dto.request.PostRequestDto;
import com.ortopunkt.logging.ServiceLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ManualInstaCommand extends ManualStatsCommandBase {

    private final SMMInputParser parser;
    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    public ManualInstaCommand(
            SMMInputParser parser,
            RestTemplate restTemplate,
            @Value("${crm.url}") String crmUrl
    ) {
        super("/инста", "INSTAGRAM", crmUrl + "/api/posts/manual/insta", restTemplate);
        this.parser = parser;
    }

    @Override
    protected PostRequestDto parseInput(String text, boolean isInstagram) {
        try {
            PostRequestDto dto = parser.parseInput(text, true);
            String lower = text.toLowerCase();

            if (lower.matches(".*всего\\s*\\d+.*")) {
                String digits = lower.replaceAll(".*всего\\s*(\\d+).*", "$1").trim();
                dto.setInstaFollowersTotal(Long.parseLong(digits));
            }

            if (lower.contains("лучшее http")) {
                String url = extractUrl(text, "лучшее");
                dto.setInstaBestPostUrl(url);
            }

            if (lower.contains("худшее http")) {
                String url = extractUrl(text, "худшее");
                dto.setInstaWorstPostUrl(url);
            }

            return dto;
        } catch (Exception e) {
            log.error("Ошибка при разборе ввода для Instagram: " + e.getMessage());
            return new PostRequestDto();
        }
    }

    private String extractUrl(String text, String key) {
        int start = text.toLowerCase().indexOf(key);
        if (start == -1) return null;
        String part = text.substring(start);
        String[] tokens = part.split("\\s+");
        for (String token : tokens) {
            if (token.startsWith("http")) {
                return token.trim();
            }
        }
        return null;
    }
}