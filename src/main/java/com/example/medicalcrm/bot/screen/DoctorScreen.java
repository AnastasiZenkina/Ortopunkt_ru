package com.example.medicalcrm.bot.screen;

import com.example.medicalcrm.bot.button.ButtonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DoctorScreen {

    public void handle(Update update, AbsSender sender) {
        Long chatId = update.getMessage().getChatId();

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.enableHtml(true);
        message.setText("""
                <b>Здравствуйте, доктор!</b>
                Я — ваш ассистент в Telegram. Помогаю:

                • получать заявки от пациентов  
                • отмечать, кто записался или оперировался
                • получать аналитику по соцсетям и рекламе  
                • отвечать за вас, если вы заняты (с помощью ИИ)
                • подсказывать, готов ли пациент к платному лечению (на основе переписки)

                <b>Выберите действие:</b>
                """);
        message.setReplyMarkup(new InlineKeyboardMarkup(
                ButtonFactory.doctorMenuButtons().stream().map(List::of).toList()));

        try {
            sender.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
