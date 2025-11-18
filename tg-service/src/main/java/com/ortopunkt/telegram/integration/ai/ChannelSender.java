package com.ortopunkt.telegram.integration.ai;

import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.dto.response.PatientResponseDto;
import com.ortopunkt.logging.GlobalExceptionHandler;
import com.ortopunkt.telegram.ui.button.MenuFactory;
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

    public void send(ApplicationResponseDto app, AbsSender sender) {
        String caption = buildCaption(app);
        List<String> ids = app.getPhotoFileIds();

        if (ids != null && !ids.isEmpty()) {
            if (ids.size() == 1) {
                sendSinglePhoto(ids.get(0), caption, sender);
            } else {
                sendMediaGroup(ids, caption, sender);
            }
            sendButtons(app, sender);
            return;
        }

        sendTextMessage(app, caption, sender);
    }

    private String buildCaption(ApplicationResponseDto app) {
        PatientResponseDto patient = app.getPatient();
        String patientName = patient != null ? patient.getName() : "Без имени";
        String username = patient != null && patient.getUsername() != null ? patient.getUsername() : "нет username";
        String text = app.getText() != null ? app.getText() : "";

        return """
            <b>%s</b> (@%s)
            %s
            """.formatted(patientName, username, text).trim();
    }

    private void sendSinglePhoto(String fileId, String caption, AbsSender sender) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(CHANNEL_ID);
        photo.setPhoto(new InputFile(fileId));
        if (!caption.isEmpty()) {
            photo.setCaption(caption);
            photo.setParseMode("HTML");
        }
        try {
            sender.execute(photo);
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
        }
    }

    private void sendMediaGroup(List<String> ids, String caption, AbsSender sender) {
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
            GlobalExceptionHandler.logError(e);
        }
    }

    private void sendButtons(ApplicationResponseDto app, AbsSender sender) {
        SendMessage buttons = new SendMessage();
        buttons.setChatId(CHANNEL_ID);
        buttons.setText("Выберите действие");
        buttons.setReplyMarkup(MenuFactory.updatedKeyboard(app));
        buttons.enableHtml(true);
        try {
            sender.execute(buttons);
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
        }
    }

    private void sendTextMessage(ApplicationResponseDto app, String caption, AbsSender sender) {
        SendMessage msg = new SendMessage();
        msg.setChatId(CHANNEL_ID);
        msg.setText(caption);
        msg.setReplyMarkup(MenuFactory.updatedKeyboard(app));
        msg.enableHtml(true);
        try {
            sender.execute(msg);
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
        }
    }
}