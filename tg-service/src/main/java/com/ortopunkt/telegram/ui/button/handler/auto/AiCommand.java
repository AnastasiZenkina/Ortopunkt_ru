package com.ortopunkt.telegram.ui.button.handler.auto;

import com.ortopunkt.logging.ServiceLogger;
import com.ortopunkt.dto.response.AnalysisResult;
import com.ortopunkt.dto.response.BotUserResponseDto;
import com.ortopunkt.telegram.client.CrmClient;
import com.ortopunkt.telegram.integration.social.AiService;
import com.ortopunkt.telegram.ui.button.ButtonCommand;
import com.ortopunkt.telegram.ui.button.MenuFactory;
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

    private static boolean enabled = true;
    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    private final AiService aiService;
    private final CrmClient crmClient;

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

        if (data.startsWith("AI_ANALYZE_")) {
            Long appId = Long.parseLong(data.replace("AI_ANALYZE_", ""));
            AnalysisResult result = aiService.analyzeApplication(appId);
            if (result == null) return;

            SendMessage msg = new SendMessage(chatId.toString(), result.toTelegramMessage());
            try {
                sender.execute(msg);
            } catch (Exception e) {
                log.error("Ошибка при отправке результата AI-анализа: " + e.getMessage());
            }
            return;
        }

        toggle();

        BotUserResponseDto user = crmClient.getBotUser(chatId);
        String role = user != null && user.getRole() != null ? user.getRole() : "PATIENT";

        List<List<InlineKeyboardButton>> buttons = switch (role) {
            case "DOCTOR" -> MenuFactory.doctorMenuButtons();
            case "SMM" -> MenuFactory.smmMenuButtons();
            default -> List.of();
        };

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);

        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup.setChatId(chatId.toString());
        editMarkup.setMessageId(messageId);
        editMarkup.setReplyMarkup(markup);

        try {
            sender.execute(editMarkup);
        } catch (Exception e) {
            log.error("Ошибка при обновлении клавиатуры AI-кнопки: " + e.getMessage());
        }
    }
}