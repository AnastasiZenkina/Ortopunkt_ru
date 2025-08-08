package com.ortopunkt.medicalcrm.bot.photo;

import com.ortopunkt.medicalcrm.config.ChannelSender;
import com.ortopunkt.medicalcrm.entity.Application;
import com.ortopunkt.medicalcrm.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BufferedPhoto {

    private final ApplicationService applicationService;
    private final ChannelSender channelSender;
    private final AbsSender botSender; // <-- этот бин нужно прописать

    @Scheduled(fixedDelay = 5000)
    public void checkBufferedPhotos() {
        for (Long chatId : PhotoCollector.getChatIds()) {
            if (PhotoCollector.isReady(chatId)) {
                List<Application> apps = PhotoCollector.collect(chatId);
                if (apps.isEmpty()) return;

                List<String> allPhotos = new ArrayList<>();
                String commonText = "";

                for (Application a : apps) {
                    if (a.getPhotoFileIds() != null) {
                        allPhotos.addAll(a.getPhotoFileIds());
                    }
                    if (commonText.isEmpty() && a.getText() != null && !a.getText().isEmpty()) {
                        commonText = a.getText();
                    }
                }

                if (!allPhotos.isEmpty()) {
                    Application combined = new Application();
                    combined.setPatient(apps.get(0).getPatient());
                    combined.setCreatedAt(LocalDate.now());
                    combined.setText(commonText);
                    combined.setPhotoFileIds(allPhotos);

                    applicationService.saveApplication(combined);
                    channelSender.send(combined, botSender); // здесь обычный бин, без getInstance
                }

                SendMessage reply = new SendMessage();
                reply.setChatId(chatId.toString());
                reply.setText("Спасибо за фото! Мы с вами свяжемся 🧡");

                try {
                    botSender.execute(reply);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
