package com.ortopunkt.telegram.ui.photo;

import com.ortopunkt.crm.entity.Application;
import com.ortopunkt.crm.service.ApplicationService;
import com.ortopunkt.crm.service.PatientService;
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
