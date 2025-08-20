package com.ortopunkt.telegram.ui.button.handler;

import com.ortopunkt.crm.entity.Application;
import com.ortopunkt.crm.repository.ApplicationRepository;
import com.ortopunkt.logging.GlobalExceptionHandler;
import com.ortopunkt.telegram.ui.button.ButtonCommand;
import com.ortopunkt.telegram.ui.button.ButtonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MarkCommand implements ButtonCommand {

    private final ApplicationRepository applicationRepository;

    @Override
    public void handle(CallbackQuery cb, AbsSender sender) {
        Long appId = extractAppId(cb.getData());
        if (appId == null) return;

        Optional<Application> optional = applicationRepository.findById(appId);
        if (optional.isEmpty()) return;

        Application app = optional.get();

        if ("Записан".equals(app.getStatus())) {
            app.setStatus(null);
        } else {
            app.setStatus("Записан");
        }
        applicationRepository.save(app);

        InlineKeyboardMarkup markup = ButtonFactory.updatedKeyboard(app);
        EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
        edit.setChatId(cb.getMessage().getChatId().toString());
        edit.setMessageId(cb.getMessage().getMessageId());
        edit.setReplyMarkup(markup);

        try {
            sender.execute(edit);
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
        }
    }

    private Long extractAppId(String data) {
        try {
            return Long.parseLong(data.replace("MARK_", ""));
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
            return null;
        }
    }
}
