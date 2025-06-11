package com.example.medicalcrm.config;

import com.example.medicalcrm.bot.button.ButtonFactory;
import com.example.medicalcrm.entity.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChannelSender {

    private static final String CHANNEL_ID = "-1002677424734";

    public void send(Application app, AbsSender sender) {
        String patientName = app.getPatient().getName();
        String username = app.getPatient().getUsername();
        String text = app.getText() != null ? app.getText() : "";

        String caption = """
            <b>%s</b> (@%s)
            %s
            """.formatted(
                patientName != null ? patientName : "Без имени",
                username != null ? username : "нет username",
                text
        ).trim(); // убираем лишние переносы

        List<String> ids = app.getPhotoFileIds();

        if (ids != null && !ids.isEmpty()) {

            if (ids.size() == 1) {
                // ОДНО ФОТО — отправляем через SendPhoto
                SendPhoto photo = new SendPhoto();
                photo.setChatId(CHANNEL_ID);
                photo.setPhoto(new InputFile(ids.get(0)));
                if (!caption.isEmpty()) {
                    photo.setCaption(caption);
                    photo.setParseMode("HTML");
                }
                try {
                    sender.execute(photo);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                // НЕСКОЛЬКО ФОТО — одним сообщением
                List<InputMedia> mediaGroup = new ArrayList<>();
                for (int i = 0; i < Math.min(10, ids.size()); i++) {
                    InputMediaPhoto media = new InputMediaPhoto();
                    media.setMedia(ids.get(i));
                    if (i == 0 && !caption.isEmpty()) {
                        media.setCaption(caption);
                        media.setParseMode("HTML");
                    }
                    mediaGroup.add(media);
                }
                try {
                    sender.execute(new SendMediaGroup(CHANNEL_ID, mediaGroup));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Отправляем кнопки
            SendMessage buttons = new SendMessage();
            buttons.setChatId(CHANNEL_ID);
            buttons.setText("Выберите действие"); // не оставлять пустым
            buttons.setReplyMarkup(ButtonFactory.updatedKeyboard(app));
            buttons.enableHtml(true);
            try {
                sender.execute(buttons);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }

        // ЕСЛИ ФОТО НЕТ — обычное текстовое сообщение
        SendMessage msg = new SendMessage();
        msg.setChatId(CHANNEL_ID);
        msg.setText(caption);
        msg.setReplyMarkup(ButtonFactory.updatedKeyboard(app));
        msg.enableHtml(true);

        try {
            sender.execute(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
