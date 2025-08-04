package com.ortopunkt.medicalcrm.bot.ai;

import com.ortopunkt.medicalcrm.bot.button.ButtonCommand;
import com.ortopunkt.medicalcrm.entity.Application;
import com.ortopunkt.medicalcrm.service.ApplicationService;
import com.ortopunkt.medicalcrm.service.BotUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import com.ortopunkt.medicalcrm.bot.button.ButtonFactory;
import com.ortopunkt.medicalcrm.entity.BotUser;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AiCommand implements ButtonCommand {

    private static boolean enabled = true;

    private final ApplicationService applicationService;
    private final AiHttpClient aiHttpClient;
    private final BotUserService botUserService;

    public static void toggle() {
        enabled = !enabled;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    @Override
    public void handle(CallbackQuery callbackQuery, AbsSender sender) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        // 🔹 1. Обработка кнопки "ИИ-анализ" (второй ИИ)
        if (data.startsWith("AI_ANALYZE_")) {
            Long appId = Long.parseLong(data.replace("AI_ANALYZE_", ""));
            Application app = applicationService.getApplicationById(appId)
                    .orElseThrow();

            SendMessage msg = new SendMessage();
            AnalysisResult result = aiHttpClient.analyze(app.getText());
            msg.setChatId(chatId.toString());
            msg.setText(result.toTelegramMessage());// можно заменить на @канал, если нужно

            try {
                sender.execute(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }

        // 🔹 2. Включение / выключение автоответа (первый ИИ)
        toggle();

        String role = botUserService.getBotUserByTelegramId(chatId)
                .map(BotUser::getRole)
                .orElse("PATIENT");

        List<List<InlineKeyboardButton>> buttons;
        switch (role) {
            case "DOCTOR" -> buttons = ButtonFactory.doctorMenuButtons();
            case "SMM" -> buttons = ButtonFactory.smmMenuButtons();
            default -> buttons = List.of();
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);

        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup.setChatId(chatId.toString());
        editMarkup.setMessageId(messageId);
        editMarkup.setReplyMarkup(markup);

        try {
            sender.execute(editMarkup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
