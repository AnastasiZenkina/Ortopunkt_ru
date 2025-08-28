package com.ortopunkt.telegram.ui.button.handler;

import com.ortopunkt.crm.entity.Application;
import com.ortopunkt.crm.service.ApplicationService;
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
public class OperatedPaidCommand implements ButtonCommand {

    private final ApplicationService applicationService;

    @Override
    public void handle(CallbackQuery cb, AbsSender sender) {
        Long appId = parseId(cb.getData(), "OPERATED_PAID_");
        if (appId == null) return;

        try {
            Application app = applicationService.getApplication(appId);
            app.setStatus("Прооперирован платно");
            applicationService.saveApplication(app);

            InlineKeyboardMarkup markup = ButtonFactory.updatedKeyboard(app);

            EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
            edit.setChatId(cb.getMessage().getChatId().toString());
            edit.setMessageId(cb.getMessage().getMessageId());
            edit.setReplyMarkup(markup);

            sender.execute(edit);
        } catch (Exception e) {
            com.ortopunkt.logging.GlobalExceptionHandler.logError(e);
        }
    }

    private Long parseId(String data, String prefix) {
        try {
            return (data != null && data.startsWith(prefix))
                    ? Long.parseLong(data.substring(prefix.length()))
                    : null;
        } catch (Exception e) {
            com.ortopunkt.logging.GlobalExceptionHandler.logError(e);
            return null;
        }
    }
}