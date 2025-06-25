package com.ortopunkt.medicalcrm.bot.button;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import com.ortopunkt.medicalcrm.bot.button.ButtonFactory;
import com.ortopunkt.medicalcrm.service.BotUserService;
import com.ortopunkt.medicalcrm.entity.BotUser;


import java.util.List;

@RequiredArgsConstructor
@Component
public class AiCommand implements ButtonCommand{

    private static boolean enabled = true;
    private final BotUserService botUserService;

    public static boolean isEnabled(){
        return enabled;
    }

    public static void toggle(){
        enabled = !enabled;
    }

    @Override
    public void handle(CallbackQuery callbackQuery, AbsSender sender) {
        // Переключаем флаг
        toggle();

        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        String role = botUserService.getBotUserByTelegramId(chatId)
                .map(BotUser::getRole)
                .orElse("PATIENT");

        List<List<InlineKeyboardButton>> buttons;
        switch (role){
            case "DOCTOR" -> buttons = ButtonFactory.doctorMenuButtons();
            case "SMM" -> buttons = ButtonFactory.smmMenuButtons();
            default -> buttons = List.of();
        }

        // Новая клавиатура
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);

        // Обновляем текст кнопки
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup.setChatId(chatId.toString());
        editMarkup.setMessageId(messageId);
        editMarkup.setReplyMarkup(markup);


        try {
            sender.execute(editMarkup);
        } catch (Exception e) {
            e.printStackTrace(); // можно добавить логгирование
        }
    }

}
