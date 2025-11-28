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
            return restTemplate.getForObject(
                    crmUrl + "/api/applications/" + id,
                    ApplicationResponseDto.class
            );
        } catch (Exception e) {
            log.error("Ошибка при получении заявки " + id + ": " + e.getMessage());
            return null;
        }
    }

    public void updateApplicationStatus(Long id, String status) {
        String url = crmUrl + "/api/applications/" + id + "/status?status=" + status;
        restTemplate.put(url, null);
    }

    public void updatePatientPaymentStatus(Long patientId, String value) {
        String url = crmUrl + "/api/patients/" + patientId + "/payment-status?value=" + value;
        restTemplate.put(url, null);
    }

    public void markAsAnswered(Long id) {
        String url = crmUrl + "/api/applications/" + id + "/status?status=HUMAN_ANSWERED";
        restTemplate.put(url, null);
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
}