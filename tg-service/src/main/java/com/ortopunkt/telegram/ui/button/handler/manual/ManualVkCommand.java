package com.ortopunkt.telegram.ui.button.handler.manual;

import com.ortopunkt.dto.request.PostRequestDto;
import com.ortopunkt.logging.ServiceLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ManualVkCommand extends ManualStatsCommandBase {

    private final SMMInputParser parser;
    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    public ManualVkCommand(
            SMMInputParser parser,
            RestTemplate restTemplate,
            @Value("${crm.url}") String crmUrl
    ) {
        super("/вк", "VK", crmUrl + "/api/posts/manual/vk", restTemplate);
        this.parser = parser;
    }

    @Override
    protected PostRequestDto parseInput(String text, boolean isInstagram) {
        try {
            PostRequestDto dto = parser.parseInput(text, false);
            log.info("DTO VK: " + dto);
            return dto;
        } catch (Exception e) {
            log.error("Ошибка при разборе ручных данных ВК: " + e.getMessage());
            return new PostRequestDto();
        }
    }
}