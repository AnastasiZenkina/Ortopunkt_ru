package com.ortopunkt.telegram.ui.screen;

import com.ortopunkt.crm.service.ApplicationService;
import com.ortopunkt.logging.GlobalExceptionHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class TargetScreen {

    public void handle(Update update, AbsSender sender, ApplicationService applicationService) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());

        message.setText("""
                    👩‍🎨 Привет! Вы вошли как 
                """);

        if (text.equalsIgnoreCase("/target") || text.equalsIgnoreCase("/таргет")) {
            message.setText("Отчет");
        }

        try {
            sender.execute(message);
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
        }
    }
}
