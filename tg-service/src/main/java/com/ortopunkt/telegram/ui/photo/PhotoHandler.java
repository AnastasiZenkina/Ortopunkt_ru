package com.ortopunkt.telegram.ui.photo;

import com.ortopunkt.dto.request.ApplicationRequestDto;
import com.ortopunkt.dto.response.ApplicationResponseDto;
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
            String fileId = msg.getPhoto()
                    .get(msg.getPhoto().size() - 1)
                    .getFileId();

            String firstName = msg.getFrom().getFirstName();
            String lastName = msg.getFrom().getLastName();
            String fullName = (firstName != null ? firstName : "")
                    + (lastName != null ? " " + lastName : "");
            fullName = fullName.trim();
            if (fullName.isBlank()) fullName = "Без имени";

            ApplicationRequestDto dto = new ApplicationRequestDto();
            dto.setTgId(chatId.toString());
            dto.setUsername(msg.getFrom().getUserName());
            dto.setName(fullName);
            dto.setText(caption);
            dto.setPhotoFileIds(List.of(fileId));

            ApplicationResponseDto saved = crmClient.sendApplication(dto);
            saved.setPhotoFileIds(List.of(fileId));

            PhotoCollector.add(chatId, saved);
            log.info("Фото получено и сохранено для chatId " + chatId);

        } catch (Exception e) {
            log.error("Ошибка при обработке фото для chatId " + chatId + ": " + e.getMessage());
        }
    }
}