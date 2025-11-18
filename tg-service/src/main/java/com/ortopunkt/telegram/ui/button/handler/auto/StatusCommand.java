package com.ortopunkt.telegram.ui.button.handler.auto;

import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import com.ortopunkt.telegram.client.CrmClient;
import com.ortopunkt.telegram.ui.button.ButtonCommand;
import com.ortopunkt.telegram.ui.button.ButtonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StatusCommand implements ButtonCommand {

    private final CrmClient crmClient;
    private final OperatedCommand operatedCommand;
    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    @Override
    public void handle(CallbackQuery cb, AbsSender sender) {
        String data = cb.getData();
        Long appId = parseId(data);
        if (appId == null) return;

        try {
            if (data.startsWith("OPERATED_PAID_") || data.startsWith("OPERATED_QUOTA_")) {
                operatedCommand.handle(cb, sender);
                return;
            }

            ApplicationResponseDto app = crmClient.getApplication(appId);
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                    List.of(
                            List.of(ButtonFactory.operatedPaidButton(appId)),
                            List.of(ButtonFactory.operatedQuotaButton(appId))
                    )
            );

            EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
            edit.setChatId(cb.getMessage().getChatId().toString());
            edit.setMessageId(cb.getMessage().getMessageId());
            edit.setReplyMarkup(markup);
            sender.execute(edit);

        } catch (Exception e) {
            log.error("Ошибка при обработке статуса: " + e.getMessage());
        }
    }

    private Long parseId(String data) {
        try {
            String id = data.replaceAll("[^0-9]", "");
            return id.isEmpty() ? null : Long.parseLong(id);
        } catch (Exception e) {
            log.error("Ошибка при разборе ID: " + e.getMessage());
            return null;
        }
    }
}