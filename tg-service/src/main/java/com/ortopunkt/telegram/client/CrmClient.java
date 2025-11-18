package com.ortopunkt.telegram.client;

import com.ortopunkt.dto.request.PatientRequestDto;
import com.ortopunkt.dto.response.PatientResponseDto;
import com.ortopunkt.dto.request.ApplicationRequestDto;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.dto.request.BotUserRequestDto;
import com.ortopunkt.dto.response.BotUserResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CrmClient {

    private final RestTemplate restTemplate;
    private final ServiceLogger log = new ServiceLogger(getClass(), "CRM");

    @Value("${crm.url}")
    private String crmUrl;

    public PatientResponseDto sendPatient(PatientRequestDto dto) {
        return restTemplate.postForObject(
                crmUrl + "/api/patients",
                dto,
                PatientResponseDto.class
        );
    }

    public ApplicationResponseDto sendApplication(ApplicationRequestDto dto) {
        return restTemplate.postForObject(
                crmUrl + "/api/applications",
                dto,
                ApplicationResponseDto.class
        );
    }

    public ApplicationResponseDto getApplication(Long id) {
        try {
            ApplicationResponseDto response = restTemplate.getForObject(
                    crmUrl + "/api/applications/" + id,
                    ApplicationResponseDto.class
            );
            log.info("Ответ CRM для заявки " + id + ": " + response);
            return response;
        } catch (Exception e) {
            log.error("Ошибка при получении заявки " + id + ": " + e.getMessage());
            return null;
        }
    }

    public void updateApplicationStatus(Long id, String status) {
        String url = crmUrl + "/api/applications/" + id + "/status?status=" + status;

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                restTemplate.put(url, null);
                log.info("Статус заявки " + id + " обновлён на: " + status);
                return;
            } catch (Exception e) {
                if (attempt == 3) {
                    log.error("Ошибка при обновлении статуса (3 попытки): " + e.getMessage());
                } else {
                    try {
                        Thread.sleep(1000L * attempt);
                    } catch (InterruptedException ignored) {}
                }
            }
        }
    }

    public void markAsAnswered(Long id) {
        String url = crmUrl + "/api/applications/" + id + "/status?status=HUMAN_ANSWERED";
        try {
            restTemplate.put(url, null);
        } catch (Exception e) {
            log.error("Ошибка при отметке заявки как отвеченной: " + e.getMessage());
        }
    }

    public BotUserResponseDto getBotUser(Long telegramId) {
        return restTemplate.getForObject(
                crmUrl + "/api/bot-users/" + telegramId,
                BotUserResponseDto.class
        );
    }

    public BotUserResponseDto createBotUser(BotUserRequestDto dto) {
        return restTemplate.postForObject(
                crmUrl + "/api/bot-users",
                dto,
                BotUserResponseDto.class
        );
    }

    public void changeUserRole(Long chatId, String newRole) {
        restTemplate.put(
                crmUrl + "/api/bot-users/" + chatId + "/role?role=" + newRole,
                null
        );
    }

    public List<ApplicationResponseDto> getAllApplications() {
        ApplicationResponseDto[] array = restTemplate.getForObject(
                crmUrl + "/api/applications",
                ApplicationResponseDto[].class
        );
        return array != null ? List.of(array) : List.of();
    }

    public ApplicationResponseDto savePatientMessage(Long chatId, String username, String fullName, String text) {
        ApplicationRequestDto dto = new ApplicationRequestDto();
        dto.setText(text);
        dto.setSource("telegram");
        dto.setChannel(username != null ? username : fullName);
        dto.setUsername(username);
        dto.setFullName(fullName);
        dto.setChatId(chatId);

        return restTemplate.postForObject(
                crmUrl + "/api/applications/from-message",
                dto,
                ApplicationResponseDto.class
        );
    }
}