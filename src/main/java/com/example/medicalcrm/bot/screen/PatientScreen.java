package com.example.medicalcrm.bot.screen;

import com.example.medicalcrm.bot.photo.PhotoHandler;
import com.example.medicalcrm.config.ChannelSender;
import com.example.medicalcrm.entity.Application;
import com.example.medicalcrm.service.ApplicationService;
import com.example.medicalcrm.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class PatientScreen {

    private final PatientService patientService;
    private final ApplicationService applicationService;
    private final ChannelSender channelSender;
    private final PhotoHandler photoHandler;

    public void handle(Update update, AbsSender sender) {
        Long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();

        String fullName = (firstName != null ? firstName : "") +
                (lastName != null ? " " + lastName : "");

        Message msg = update.getMessage();
        String replyText = null;

        if (msg.hasText()) {
            String text = msg.getText();

            if (text.equalsIgnoreCase("/start")) {
                replyText = "Айдыс Вячеславович на связи! Чем могу помочь?";
            } else {
                Application app = patientService.processMessage(chatId, username, fullName, text);
                applicationService.saveApplication(app);
                channelSender.send(app, sender);
                replyText = "Спасибо за сообщение! Мы с вами свяжемся 🧡";
            }

        } else if (msg.hasPhoto()) {
            photoHandler.handle(update, sender);
            return;
        }

        if (replyText != null) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId.toString());
            message.setText(replyText);
            try {
                sender.execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
