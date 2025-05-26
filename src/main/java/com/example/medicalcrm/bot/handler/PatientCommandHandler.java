package com.example.medicalcrm.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class PatientCommandHandler {


    public void handle(Update update, AbsSender sender) {
        Long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());

        if (update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (text.equalsIgnoreCase("/start")) {
                message.setText("Добро пожаловать, " + username + "! Чем могу помочь?");
            } else {
                message.setText("Спасибо за сообщение! Мы с вами свяжемся 🧡");
            }
        } else if (update.getMessage().hasPhoto()) {
            message.setText("Фото получено! Спасибо, мы передадим его врачу.");
        }

        try {
            sender.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
