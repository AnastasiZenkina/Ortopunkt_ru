package com.ortopunkt.telegram.ui.button.handler.auto;

import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import com.ortopunkt.telegram.client.CrmClient;
import com.ortopunkt.telegram.ui.button.ButtonCommand;
import com.ortopunkt.telegram.ui.button.MenuFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class MarkCommand implements ButtonCommand {

    private final CrmClient crmClient;
    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    @Override
    public void handle(CallbackQuery cb, AbsSender sender) {
        Long appId = extractAppId(cb.getData());
        if (appId == null) return;

        try {
            ApplicationResponseDto app = crmClient.getApplication(appId);
            String newStatus = "Записан".equals(app.getStatus()) ? "Новый" : "Записан";
            crmClient.updateApplicationStatus(appId, newStatus);

            ApplicationResponseDto updatedApp = crmClient.getApplication(appId);
            InlineKeyboardMarkup markup = MenuFactory.updatedKeyboard(updatedApp);

            EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
            edit.setChatId(cb.getMessage().getChatId().toString());
            edit.setMessageId(cb.getMessage().getMessageId());
            edit.setReplyMarkup(markup);
            sender.execute(edit);

        } catch (Exception e) {
            log.error("Ошибка при обновлении кнопок 'Записан': " + e.getMessage());
        }
    }

    private Long extractAppId(String data) {
        try {
            return Long.parseLong(data.replace("MARK_", ""));
        } catch (Exception e) {
            log.error("Ошибка при извлечении ID из callback: " + e.getMessage());
            return null;
        }
    }
}