package com.ortopunkt.telegram.ui;

import com.ortopunkt.dto.request.BotUserRequestDto;
import com.ortopunkt.dto.response.BotUserResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class BotRoleResolver {

    private final RestTemplate restTemplate;
    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    @Value("${crm.url}")
    private String crmUrl;

    public String resolveRole(Long chatId, String username) {
        try {
            ResponseEntity<BotUserResponseDto> response = restTemplate.getForEntity(
                    crmUrl + "/api/bot-users/" + chatId,
                    BotUserResponseDto.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().getRole() != null) {
                BotUserResponseDto user = response.getBody();
                log.info("Пользователь найден в CRM: @" + username + " (role=" + user.getRole() + ")");
                return user.getRole();
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                BotUserRequestDto dto = new BotUserRequestDto();
                dto.setTelegramId(chatId);
                dto.setUsername(username);
                dto.setRole("PATIENT");
                restTemplate.postForObject(crmUrl + "/api/bot-users", dto, BotUserResponseDto.class);
                log.info("Создан новый пользователь @" + username + " как PATIENT");
                return "PATIENT";
            } else {
                log.error("Ошибка при обращении к CRM: " + e.getMessage());
            }
        } catch (Exception e) {
            log.error("Ошибка при обращении к CRM: " + e.getMessage());
        }
        return "PATIENT";
    }
}