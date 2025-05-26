package com.example.medicalcrm.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class SmmCommandHandler {


    public void handle(Update update, AbsSender sender) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());

        if (text.equalsIgnoreCase("/smm") || text.equalsIgnoreCase("/смм")) {
            message.setText("""
                📊 SMM-отчёт за неделю

                👥 VK:
                👁️ Охват: 6 120
                🤝 Вовлечённость: 4.2%
                📌 Подписки: 84
                🔁 Репостов: 28
                💬 Комментарии: 19

                📷 Instagram:
                👁️ Охват: 3 420
                🤝 Вовлечённость: 5.1%
                📌 Подписки: 44
                🔁 Репостов: 14
                💬 Комментарии: 22

                📉 Слабые посты:
                VK:  “Плоскостопие” — 230 охвата, 1 лайк
                Insta: “Вальгус у детей” — 260 охвата, 1 заявка
            """);
        } else {
            message.setText("Здравствуйте. Используйте команду /smm для получения отчёта.");
        }

        try {
            sender.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}