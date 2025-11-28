package com.ortopunkt.telegram.ui.screen;

import com.ortopunkt.dto.request.ApplicationRequestDto;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import com.ortopunkt.telegram.client.CrmClient;
import com.ortopunkt.telegram.integration.ai.ChannelSender;
import com.ortopunkt.telegram.ui.photo.PhotoHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class PatientScreen {

    private final CrmClient crmClient;
    private final ChannelSender channelSender;
    private final PhotoHandler photoHandler;
    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    public void handle(Update update, AbsSender sender) {

        Message msg = update.getMessage();
        if (msg == null || msg.getFrom() == null) return;

        Long chatId = msg.getChatId();
        String username = msg.getFrom().getUserName();
        String firstName = msg.getFrom().getFirstName();
        String lastName = msg.getFrom().getLastName();

        String fullName = (firstName != null ? firstName : "") +
                (lastName != null ? " " + lastName : "");
        fullName = fullName.trim();
        if (fullName.isBlank()) fullName = "Без имени";

        String replyText = null;

        if (msg.hasText()) {
            String text = msg.getText();

            if (text.equalsIgnoreCase("/start")) {
                replyText = "Айдыс Вячеславович на связи! Чем могу помочь?";
            } else {
                ApplicationRequestDto dto = new ApplicationRequestDto();
                dto.setTgId(chatId.toString());
                dto.setUsername(username);
                dto.setName(fullName);
                dto.setText(text);

                ApplicationResponseDto app = crmClient.sendApplication(dto);
                channelSender.send(app, sender);
                log.info("Получено текстовое сообщение от @" + username + " (" + chatId + ")");
            }

        } else if (msg.hasPhoto()) {
            photoHandler.handle(update, sender);
            log.info("Получено фото от @" + username + " (" + chatId + ")");
            return;
        }

        if (replyText != null) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId.toString());
            message.setText(replyText);
            try {
                sender.execute(message);
                log.info("Ответ отправлен пользователю @" + username);
            } catch (Exception e) {
                log.error("Ошибка при отправке сообщения: " + e.getMessage());
            }
        }
    }
}