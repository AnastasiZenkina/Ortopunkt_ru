package com.ortopunkt.telegram.ui.button.handler;

import com.ortopunkt.logging.GlobalExceptionHandler;
import com.ortopunkt.telegram.aiintegration.AiHttpClient;
import com.ortopunkt.crm.entity.Application;
import com.ortopunkt.crm.entity.BotUser;
import com.ortopunkt.crm.service.ApplicationService;
import com.ortopunkt.crm.service.BotUserService;
import com.ortopunkt.telegram.ui.button.ButtonCommand;
import com.ortopunkt.telegram.ui.button.ButtonFactory;
import com.ortopunkt.dto.response.AnalysisResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AiCommand implements ButtonCommand {

    private static boolean enabled = true; // как ты и хотела — оставляем общий переключатель

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

        // ИИ-анализ конкретной заявки
        if (data.startsWith("AI_ANALYZE_")) {
            Long appId = Long.parseLong(data.replace("AI_ANALYZE_", ""));
            Application app = applicationService.getApplicationById(appId).orElse(null);
            if (app == null) return;

            AnalysisResult result = aiHttpClient.analyze(app.getText());
            SendMessage msg = new SendMessage(chatId.toString(), result.toTelegramMessage());
            try {
                sender.execute(msg);
            } catch (Exception e) {
                GlobalExceptionHandler.logError(e);
            }
            return;
        }

        // Вкл/выкл ИИ автоответчик (оставлено как есть)
        toggle();

        String role = botUserService.getBotUserByTelegramId(chatId)
                .map(BotUser::getRole)
                .orElse("PATIENT");

        List<List<InlineKeyboardButton>> buttons = switch (role) {
            case "DOCTOR" -> ButtonFactory.doctorMenuButtons();
            case "SMM"    -> ButtonFactory.smmMenuButtons();
            default       -> List.of();
        };

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);

        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup.setChatId(chatId.toString());
        editMarkup.setMessageId(messageId);
        editMarkup.setReplyMarkup(markup);

        try {
            sender.execute(editMarkup);
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
        }
    }
}
