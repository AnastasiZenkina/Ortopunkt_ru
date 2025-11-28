package com.ortopunkt.telegram.ui.button;

import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.telegram.ui.button.handler.auto.AiCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class MenuFactory {

    public static List<List<InlineKeyboardButton>> doctorMenuButtons() {
        return List.of(
                List.of(patientsButton()),
                List.of(InlineKeyboardButton.builder()
                        .text("Отчёт")
                        .callbackData("DOCTOR_REPORT")
                        .build()),
                List.of(ButtonFactory.aiButton(AiCommand.isEnabled())),
                List.of(InlineKeyboardButton.builder()
                        .text("Сменить роль")
                        .callbackData("CHANGE_ROLE")
                        .build())
        );
    }

    public static List<List<InlineKeyboardButton>> smmMenuButtons() {
        return List.of(
                List.of(patientsButton()),
                List.of(InlineKeyboardButton.builder()
                        .text("Отчёт по соцсетям")
                        .callbackData("SMM_REPORT")
                        .build()),
                List.of(ButtonFactory.aiButton(AiCommand.isEnabled())),
                List.of(InlineKeyboardButton.builder()
                        .text("Сменить роль")
                        .callbackData("CHANGE_ROLE")
                        .build())
        );
    }

    public static List<List<InlineKeyboardButton>> targetMenuButtons() {
        return List.of(
                List.of(patientsButton()),
                List.of(InlineKeyboardButton.builder()
                        .text("Отчёт по платной рекламе")
                        .callbackData("TARGET_REPORT")
                        .build()),
                List.of(InlineKeyboardButton.builder()
                        .text("Сменить роль")
                        .callbackData("CHANGE_ROLE")
                        .build())
        );
    }

    private static InlineKeyboardButton patientsButton() {
        return InlineKeyboardButton.builder()
                .text("Заявки")
                .callbackData("DOCTOR_PATIENTS")
                .build();
    }

    public static InlineKeyboardMarkup updatedKeyboard(ApplicationResponseDto app) {
        return new InlineKeyboardMarkup(List.of(
                List.of(
                        ButtonFactory.answerChatButton(app),
                        ButtonFactory.markButton(app.getId(), "BOOKED".equals(app.getStatus()))
                ),
                List.of(
                        ButtonFactory.aiAnalysisButton(app.getId()),
                        ButtonFactory.statusButton(app.getId())
                )
        ));
    }
}