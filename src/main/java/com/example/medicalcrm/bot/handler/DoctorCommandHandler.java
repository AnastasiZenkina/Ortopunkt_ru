package com.example.medicalcrm.bot.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class DoctorCommandHandler {

    private final AbsSender sender;

    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());

        if (text.equalsIgnoreCase("/отчёт") || text.equalsIgnoreCase("/отчет")) {
            message.setText("🧠 Еженедельный отчёт:\n(Тут будет аналитика — пока заглушка)");
        } else if (text.equalsIgnoreCase("/пациенты")) {
            message.setText("📋 Список пациентов загружается...\n(тут позже появятся карточки)");
        } else {
            message.setText("Здравствуйте, доктор! Используйте /отчёт или /пациенты.");
        }

        try {
            sender.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
