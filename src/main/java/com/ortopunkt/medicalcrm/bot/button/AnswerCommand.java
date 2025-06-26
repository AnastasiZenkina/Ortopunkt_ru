package com.ortopunkt.medicalcrm.bot.button;

import com.ortopunkt.medicalcrm.entity.Application;
import com.ortopunkt.medicalcrm.service.ApplicationService;
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
        Long appId = Long.valueOf(query.getData().split("_")[1]);

        // Обновляем статус заявки
        applicationService.markAsAnswered(appId);

        // Загружаем заявку и создаём новую клавиатуру
        Application app = applicationService.getApplication(appId);
        InlineKeyboardMarkup updatedMarkup = ButtonFactory.updatedKeyboard(app);

        // Отправляем обновлённую клавиатуру
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup.setChatId(query.getMessage().getChatId().toString());
        editMarkup.setMessageId(query.getMessage().getMessageId());
        editMarkup.setReplyMarkup(updatedMarkup);

        try {
            sender.execute(editMarkup);
        } catch (Exception e) {
            e.printStackTrace(); // Или лог
        }
    }
}
