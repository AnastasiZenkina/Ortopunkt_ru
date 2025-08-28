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
public class AnswerCommand implements ButtonCommand {

    private final ApplicationService applicationService;

    @Override
    public void handle(CallbackQuery query, AbsSender sender) {
        Long appId = parseId(query.getData(), "ANSWER_");
        if (appId == null) return;

        applicationService.markAsAnswered(appId);

        Application app = applicationService.getApplication(appId);
        InlineKeyboardMarkup updatedMarkup = ButtonFactory.updatedKeyboard(app);

        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup.setChatId(query.getMessage().getChatId().toString());
        editMarkup.setMessageId(query.getMessage().getMessageId());
        editMarkup.setReplyMarkup(updatedMarkup);

        try {
            sender.execute(editMarkup);
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
        }
    }

    private Long parseId(String data, String prefix) {
        try {
            return (data != null && data.startsWith(prefix))
                    ? Long.parseLong(data.substring(prefix.length()))
                    : null;
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
            return null;
        }
    }
}
