package com.ortopunkt.telegram.ui.button.handler.auto;

import com.ortopunkt.dto.response.BotUserResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import com.ortopunkt.telegram.ui.button.ButtonCommand;
import com.ortopunkt.telegram.ui.screen.DoctorScreen;
import com.ortopunkt.telegram.ui.screen.SmmScreen;
import com.ortopunkt.telegram.ui.screen.TargetScreen;
import com.ortopunkt.telegram.client.CrmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleChangeCommand implements ButtonCommand {

    private final DoctorScreen doctorScreen;
    private final SmmScreen smmScreen;
    private final TargetScreen targetScreen;
    private final CrmClient crmClient;
    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    @Override
    public void handle(CallbackQuery cb, AbsSender sender) {
        String data = cb.getData();
        String chatId = cb.getMessage().getChatId().toString();

        try {
            if ("CHANGE_ROLE".equals(data)) {
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                        List.of(
                                List.of(
                                        InlineKeyboardButton.builder().text("Доктор").callbackData("ROLE_DOCTOR").build(),
                                        InlineKeyboardButton.builder().text("SMM").callbackData("ROLE_SMM").build(),
                                        InlineKeyboardButton.builder().text("Таргет").callbackData("ROLE_TARGET").build()
                                )
                        )
                );

                EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
                edit.setChatId(chatId);
                edit.setMessageId(cb.getMessage().getMessageId());
                edit.setReplyMarkup(markup);
                sender.execute(edit);
                return;
            }

            if (data.startsWith("ROLE_")) {
                String newRole = data.replace("ROLE_", "");
                BotUserResponseDto current = crmClient.getBotUser(Long.parseLong(chatId));

                if (current != null && newRole.equalsIgnoreCase(current.getRole())) {
                    EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
                    edit.setChatId(chatId);
                    edit.setMessageId(cb.getMessage().getMessageId());

                    switch (newRole.toUpperCase()) {
                        case "DOCTOR" -> edit.setReplyMarkup(new InlineKeyboardMarkup(
                                com.ortopunkt.telegram.ui.button.MenuFactory.doctorMenuButtons()
                        ));
                        case "SMM" -> edit.setReplyMarkup(new InlineKeyboardMarkup(
                                com.ortopunkt.telegram.ui.button.MenuFactory.smmMenuButtons()
                        ));
                        case "TARGET" -> edit.setReplyMarkup(new InlineKeyboardMarkup(
                                com.ortopunkt.telegram.ui.button.MenuFactory.targetMenuButtons()
                        ));
                    }

                    sender.execute(edit);
                    return;
                }

                crmClient.changeUserRole(Long.parseLong(chatId), newRole);

                EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
                edit.setChatId(chatId);
                edit.setMessageId(cb.getMessage().getMessageId());
                edit.setReplyMarkup(null);
                sender.execute(edit);

                Update update = new Update();
                update.setCallbackQuery(cb);

                switch (newRole.toUpperCase()) {
                    case "DOCTOR" -> doctorScreen.handle(update, sender);
                    case "SMM" -> smmScreen.handle(update, sender, crmClient);
                    case "TARGET" -> targetScreen.handle(update, sender, crmClient);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при смене роли: " + e.getMessage());
        }
    }
}