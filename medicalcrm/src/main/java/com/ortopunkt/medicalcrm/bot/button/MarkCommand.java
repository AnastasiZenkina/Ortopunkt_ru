package com.ortopunkt.medicalcrm.bot.button;

import com.ortopunkt.medicalcrm.entity.Application;
import com.ortopunkt.medicalcrm.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MarkCommand implements ButtonCommand {

    private final ApplicationRepository applicationRepository;

    @Override
    public void handle(CallbackQuery cb, AbsSender sender) {
        String data = cb.getData();
        Long appId = extractAppId(data);
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

        EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
        edit.setChatId(cb.getMessage().getChatId().toString());
        edit.setMessageId(cb.getMessage().getMessageId());
        edit.setReplyMarkup(ButtonFactory.updatedKeyboard(app));

        try {
            sender.execute(edit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Long extractAppId(String data) {
        try {
            return Long.parseLong(data.replace("MARK_", ""));
        } catch (Exception e) {
            return null;
        }
    }
}
