package com.ortopunkt.telegram.ui.photo;

import com.ortopunkt.dto.request.ApplicationRequestDto;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import com.ortopunkt.telegram.client.CrmClient;
import com.ortopunkt.telegram.integration.ai.ChannelSender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BufferedPhoto {

    private final CrmClient crmClient;
    private final ChannelSender channelSender;
    private final AbsSender botSender;
    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    @Scheduled(fixedDelay = 5000)
    public void checkBufferedPhotos() {
        for (Long chatId : PhotoCollector.getChatIds()) {
            if (!PhotoCollector.isReady(chatId)) continue;

            var apps = PhotoCollector.collect(chatId);
            if (apps.isEmpty()) continue;

            List<String> allPhotos = new ArrayList<>();
            String commonText = "";
            String username = null;
            String fullName = null;

            for (var a : apps) {
                if (a.getPhotoFileIds() != null)
                    allPhotos.addAll(a.getPhotoFileIds());

                if (commonText.isEmpty() && a.getText() != null && !a.getText().isEmpty())
                    commonText = a.getText();

                if (username == null && a.getPatient() != null)
                    username = a.getPatient().getUsername();

                if (fullName == null && a.getPatient() != null)
                    fullName = a.getPatient().getName();
            }

            if (username == null || username.isBlank())
                username = "unknown";

            if (fullName == null || fullName.isBlank())
                fullName = "Без имени";

            try {
                ApplicationRequestDto dto = new ApplicationRequestDto();
                dto.setTgId(chatId.toString());
                dto.setUsername(username);
                dto.setName(fullName);
                dto.setText(commonText);
                dto.setPhotoFileIds(allPhotos);

                ApplicationResponseDto saved = crmClient.sendApplication(dto);

                channelSender.send(saved, botSender);
                log.info("Buffered photos saved and sent for chatId " + chatId);

                botSender.execute(new SendMessage(chatId.toString(), "Спасибо за обращение и фото! Врач сейчас на операции и ответит вам как только сможет"));
            } catch (Exception e) {
                log.error("Ошибка при обработке фото для chatId " + chatId + ": " + e.getMessage());
            }
        }
    }
}