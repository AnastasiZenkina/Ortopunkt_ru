package com.ortopunkt.telegram.ui.screen;

import com.ortopunkt.logging.ServiceLogger;
import com.ortopunkt.telegram.client.CrmClient;
import com.ortopunkt.telegram.ui.button.MenuFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class SmmScreen {

    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    public void handle(Update update, AbsSender sender, CrmClient crmClient) {
        Long chatId = null;
        String text = null;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            text = update.getMessage().getText();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            text = update.getCallbackQuery().getData();
        }

        if (chatId == null) {
            log.error("Не удалось получить chatId для SmmScreen");
            return;
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setParseMode("HTML");
        message.setText("""
                <b>Привет! Вы вошли как SMM-специалист</b>
                Я помогу вам:

                • отслеживать охваты, вовлечённость, подписки и комментарии
                • видеть топ-посты и слабые публикации по VK и Instagram
                • анализировать видео по уровню досмотра
                • смотреть, какие посты дали заявки и записи
                • формировать отчёты и делиться ими с врачом

                <b>Выберите действие:</b>
                """);
        message.setReplyMarkup(new InlineKeyboardMarkup(MenuFactory.smmMenuButtons()));

        if (text != null && (text.equalsIgnoreCase("/smm") || text.equalsIgnoreCase("/смм"))) {
            message.setText("<b>Отчет</b>");
        }

        try {
            sender.execute(message);
            log.info("Отправлено меню SMM пользователю " + chatId);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения в SmmScreen: " + e.getMessage());
        }
    }
}