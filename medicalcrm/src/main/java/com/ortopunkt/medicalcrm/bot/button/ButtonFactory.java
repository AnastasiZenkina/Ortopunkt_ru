package com.ortopunkt.medicalcrm.bot.button;

import com.ortopunkt.medicalcrm.bot.ai.AiCommand;
import com.ortopunkt.medicalcrm.entity.Application;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class ButtonFactory {

    // 🔹 1. Кнопки для меню по ролям
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

    // 🔹 2. Кнопки для карточки заявки

    public static InlineKeyboardButton aiAnalysusButton(Long appId) {
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

    /* public static InlineKeyboardButton quotaButton(Long appId, boolean marked) {
        return InlineKeyboardButton.builder()
                .text(marked ? "✅ По квоте" : "По квоте")
                .callbackData("FREE_" + appId)
                .build();
    }

    public static InlineKeyboardButton paidButton(Long appId, boolean marked) {
        return InlineKeyboardButton.builder()
                .text(marked ? "✅ Платно" : "Платно")
                .callbackData("PAID_" + appId)
                .build();
    }*/

    // 🔹 3. Обновление всей клавиатуры по статусу заявки

    public static InlineKeyboardMarkup updatedKeyboard(Application app) {
        return new InlineKeyboardMarkup(List.of(
                List.of(
                        aiAnalysusButton(app.getId())
                ),
                List.of(
                        app.isAnsweredByHuman()
                                ? chatButton(app.getPatient().getUsername())
                                : answerChatButton(app),
                        markButton(app.getId(), "Записан".equals(app.getStatus()))
                )
            /*List.of(
                    quotaButton(app.getId(), "По квоте".equals(app.getPaymentStatus())),
                    paidButton(app.getId(), "Платно".equals(app.getPaymentStatus()))
            )*/
        ));
    }

}
