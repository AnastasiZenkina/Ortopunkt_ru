package com.ortopunkt.telegram.ui.button;

import com.ortopunkt.dto.response.ApplicationResponseDto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class ButtonFactory {

    public static InlineKeyboardButton aiButton(boolean enabled) {
        String text = enabled ? "Выключить ИИ автоответчик" : "Включить ИИ автоответчик";
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData("AI_BUTTON")
                .build();
    }

    public static List<List<InlineKeyboardButton>> roleChangeButtons() {
        return List.of(
                List.of(InlineKeyboardButton.builder()
                        .text("Доктор")
                        .callbackData("/doctor")
                        .build()),
                List.of(InlineKeyboardButton.builder()
                        .text("SMM")
                        .callbackData("/smm")
                        .build()),
                List.of(InlineKeyboardButton.builder()
                        .text("Таргетолог")
                        .callbackData("/target")
                        .build())
        );
    }

    public static InlineKeyboardButton aiAnalysisButton(Long appId) {
        return InlineKeyboardButton.builder()
                .text("ИИ-анализ")
                .callbackData("AI_ANALYZE_" + appId)
                .build();
    }

    public static InlineKeyboardButton answerChatButton(ApplicationResponseDto app) {
        boolean answered = "ANSWERED".equals(app.getStatus());
        return InlineKeyboardButton.builder()
                .text(answered ? "Отвечено ✅" : "Ответить")
                .callbackData("ANSWER_" + app.getId())
                .build();
    }

    public static InlineKeyboardButton chatButton(String username) {
        return InlineKeyboardButton.builder()
                .text("Перейти в чат")
                .url("https://t.me/" + username.replace("@", "").trim())
                .build();
    }

    public static InlineKeyboardButton markButton(Long appId, boolean marked) {
        return InlineKeyboardButton.builder()
                .text(marked ? "Записан ✅" : "Записать")
                .callbackData("MARK_" + appId)
                .build();
    }

    public static InlineKeyboardButton statusButton(Long appId) {
        return InlineKeyboardButton.builder()
                .text("Статус")
                .callbackData("STATUS_" + appId)
                .build();
    }

    public static InlineKeyboardButton operatedPaidButton(Long appId) {
        return InlineKeyboardButton.builder()
                .text("Платно")
                .callbackData("OPERATED_PAID_" + appId)
                .build();
    }

    public static InlineKeyboardButton operatedQuotaButton(Long appId) {
        return InlineKeyboardButton.builder()
                .text("По квоте")
                .callbackData("OPERATED_QUOTA_" + appId)
                .build();
    }
}