package com.ortopunkt.telegram.ui.photo;

import com.ortopunkt.dto.request.ApplicationRequestDto;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.dto.response.PatientResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import com.ortopunkt.telegram.client.CrmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PhotoHandler {

    private final CrmClient crmClient;
    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    public void handle(Update update, AbsSender sender) {
        Long chatId = update.getMessage().getChatId();
        Message msg = update.getMessage();
        String caption = msg.getCaption() != null ? msg.getCaption() : "";

        try {
            ApplicationRequestDto dto = new ApplicationRequestDto();
            dto.setPatientId(chatId);
            dto.setText(caption);
            dto.setSource("telegram");
            dto.setChannel("bot");

            ApplicationResponseDto saved = crmClient.sendApplication(dto);

            String fileId = msg.getPhoto().get(msg.getPhoto().size() - 1).getFileId();
            saved.setPhotoFileIds(List.of(fileId));

            String firstName = msg.getFrom().getFirstName();
            String lastName = msg.getFrom().getLastName();
            String fullName = (firstName != null ? firstName : "") +
                    (lastName != null ? " " + lastName : "");

            PatientResponseDto patient = new PatientResponseDto();
            patient.setTgId(chatId.toString());
            patient.setName(fullName.isBlank() ? "Без имени" : fullName.trim());
            patient.setUsername(msg.getFrom().getUserName());
            saved.setPatient(patient);

            PhotoCollector.add(chatId, saved);
            log.info("Фото получено и сохранено для chatId " + chatId);
        } catch (Exception e) {
            log.error("Ошибка при обработке фото для chatId " + chatId + ": " + e.getMessage());
        }
    }
}