package com.ortopunkt.telegram.ui.button;

import com.ortopunkt.crm.entity.Application;
import com.ortopunkt.telegram.ui.button.handler.AiCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class ButtonFactory {

    // ── 1) Меню по ролям ─────────────────────────────────────────────

    public static List<List<InlineKeyboardButton>> doctorMenuButtons() {
        return List.of(
                List.of(patientsButton()),
                List.of(InlineKeyboardButton.builder()
                        .text("📄 Отчёт")
                        .callbackData("DOCTOR_REPORT")
                        .build()),
                List.of(aiButton(AiCommand.isEnabled()))
        );
    }

    public static List<List<InlineKeyboardButton>> smmMenuButtons() {
        return List.of(
                List.of(patientsButton()),
                List.of(InlineKeyboardButton.builder()
                        .text("📄 Отчёт по соцсетям")
                        .callbackData("SMM_REPORT")
                        .build()),
                List.of(aiButton(AiCommand.isEnabled()))
        );
    }

    public static List<InlineKeyboardButton> targetMenuButtons() {
        return List.of(
                patientsButton(),
                InlineKeyboardButton.builder()
                        .text("📄 Отчёт по платной рекламе")
                        .callbackData("TARGET_REPORT")
                        .build()
        );
    }

    private static InlineKeyboardButton patientsButton() {
        return InlineKeyboardButton.builder()
                .text("👥 Заявки")
                .callbackData("DOCTOR_PATIENTS")
                .build();
    }

    public static InlineKeyboardButton aiButton(boolean enabled){
        String text = enabled ? "Выключить ИИ автоответчик" : "Включить ИИ автоответчик";
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData("AI_BUTTON")
                .build();
    }

    // ── 2) Карточка заявки ───────────────────────────────────────────

    public static InlineKeyboardButton aiAnalysisButton(Long appId) {
        return InlineKeyboardButton.builder()
                .text("ИИ-анализ")
                .callbackData("AI_ANALYZE_" + appId)
                .build();
    }

    public static InlineKeyboardButton answerChatButton(Application app) {
        return InlineKeyboardButton.builder()
                .text("Ответить")
                .callbackData("ANSWER_" + app.getId())
                .build();
    }

    public static InlineKeyboardButton chatButton(String username) {
        return InlineKeyboardButton.builder()
                .text("Перейти в чат")
                .url("https://t.me/" + username)
                .build();
    }

    public static InlineKeyboardButton markButton(Long appId, boolean marked) {
        return InlineKeyboardButton.builder()
                .text(marked ? "✅ Записан" : "Записать")
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
                .callbackData("STATUS_PAID_" + appId)
                .build();
    }

    public static InlineKeyboardButton operatedQuotaButton(Long appId) {
        return InlineKeyboardButton.builder()
                .text("По квоте")
                .callbackData("STATUS_QUOTA_" + appId)
                .build();
    }

    // ── 3) Сборка клавиатуры карточки ────────────────────────────────

    public static InlineKeyboardMarkup updatedKeyboard(Application app) {
        String username = (app.getPatient() != null) ? app.getPatient().getUsername() : null;

        return new InlineKeyboardMarkup(List.of(
                List.of(
                        aiAnalysisButton(app.getId()),
                        markButton(app.getId(), "Записан".equals(app.getStatus()))
                ),
                List.of(
                        (app.isAnsweredByHuman() && username != null)
                                ? chatButton(username)
                                : answerChatButton(app),
                        statusButton(app.getId())
                )
        ));
    }

    // ── 4) Подменю статуса ───────────────────────────────────────────

    public static InlineKeyboardMarkup statusSubmenu(Long appId, String currentStatus) {
        return new InlineKeyboardMarkup(List.of(
                List.of(
                        operatedPaidButton(appId),
                        operatedQuotaButton(appId)
                )
        ));
    }
}