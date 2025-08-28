package com.ortopunkt.telegram.ui.button.handler;

import com.ortopunkt.crm.entity.Application;
import com.ortopunkt.crm.service.ApplicationService;
import com.ortopunkt.logging.GlobalExceptionHandler;
import com.ortopunkt.telegram.ui.button.ButtonCommand;
import com.ortopunkt.telegram.ui.button.ButtonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class StatusCommand implements ButtonCommand {

    private final ApplicationService applicationService;

    @Override
    public void handle(CallbackQuery cb, AbsSender sender) {
        String data = cb.getData();
        Long appId = parseId(data);
        if (appId == null) return;

        try {
            Application app = applicationService.getApplication(appId);

            InlineKeyboardMarkup markup;

            if (data.startsWith("STATUS_PAID_")) {
                app.setStatus("Прооперирован платно");
                applicationService.saveApplication(app);
                markup = ButtonFactory.updatedKeyboard(app); // вернёмся к обычной клавиатуре
            } else if (data.startsWith("STATUS_QUOTA_")) {
                app.setStatus("Прооперирован по квоте");
                applicationService.saveApplication(app);
                markup = ButtonFactory.updatedKeyboard(app);
            } else {
                // если просто нажали "Статус" → показываем подменю
                markup = ButtonFactory.statusSubmenu(appId, app.getStatus());
            }

            EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
            edit.setChatId(cb.getMessage().getChatId().toString());
            edit.setMessageId(cb.getMessage().getMessageId());
            edit.setReplyMarkup(markup);

            sender.execute(edit);
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
        }
    }

    private Long parseId(String data) {
        try {
            String id = data.replaceAll("[^0-9]", "");
            return id.isEmpty() ? null : Long.parseLong(id);
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
            return null;
        }
    }
}