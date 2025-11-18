package com.ortopunkt.telegram.ui.screen;

import com.ortopunkt.logging.ServiceLogger;
import com.ortopunkt.telegram.ui.button.MenuFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class DoctorScreen {

    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    public void handle(Update update, AbsSender sender) {
        Long chatId = null;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }

        if (chatId == null) {
            log.error("Не удалось получить chatId для DoctorScreen");
            return;
        }

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
                MenuFactory.doctorMenuButtons()
        ));

        try {
            sender.execute(message);
            log.info("Меню доктора успешно отправлено (chatId=" + chatId + ")");
        } catch (Exception e) {
            log.error("Ошибка при отправке меню доктору: " + e.getMessage());
        }
    }
}