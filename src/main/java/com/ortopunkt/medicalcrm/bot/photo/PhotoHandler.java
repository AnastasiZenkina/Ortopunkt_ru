package com.ortopunkt.medicalcrm.bot.photo;

import com.ortopunkt.medicalcrm.entity.Application;
import com.ortopunkt.medicalcrm.service.ApplicationService;
import com.ortopunkt.medicalcrm.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class PhotoHandler {

    private final PatientService patientService;
    private final ApplicationService applicationService;

    public void handle(Update update, AbsSender sender) {
        Long chatId = update.getMessage().getChatId();
        Message msg = update.getMessage();
        String caption = msg.getCaption() != null ? msg.getCaption() : "";

        Application app = patientService.processPhotoMessage(msg, caption);
        applicationService.saveApplication(app);
        PhotoCollector.add(chatId, app);


    }
}
