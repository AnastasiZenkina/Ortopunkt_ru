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
public class TargetScreen {

    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    public void handle(Update update, AbsSender sender, CrmClient crmClient) {
        Long chatId = null;
        String text = "";

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            text = update.getMessage().getText();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            text = update.getCallbackQuery().getData();
        }

        if (chatId == null) {
            log.error("Не удалось получить chatId для TargetScreen");
            return;
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setParseMode("HTML");
        message.setText("""
                <b>Здравствуйте! Вы вошли как таргетолог</b>
                Я помогу вам:

                • видеть эффективность платных рекламных кампаний
                • анализировать CTR, стоимость клика и заявки
                • отслеживать конверсии в запись и операцию
                • формировать отчёты по VK Ads и другим каналам

                <b>Выберите действие:</b>
                """);
        message.setReplyMarkup(new InlineKeyboardMarkup(MenuFactory.targetMenuButtons()));

        if (text.equalsIgnoreCase("/target") || text.equalsIgnoreCase("/таргет")) {
            message.setText("<b>Отчёт</b>");
        }

        try {
            sender.execute(message);
            log.info("Отправлено меню TARGET пользователю " + chatId);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения в TargetScreen: " + e.getMessage());
        }
    }
}