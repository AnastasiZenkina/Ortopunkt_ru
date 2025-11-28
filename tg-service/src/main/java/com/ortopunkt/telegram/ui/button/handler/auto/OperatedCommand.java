package com.ortopunkt.telegram.ui.button.handler.auto;

import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import com.ortopunkt.telegram.client.CrmClient;
import com.ortopunkt.telegram.ui.button.ButtonCommand;
import com.ortopunkt.telegram.ui.button.MenuFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class OperatedCommand implements ButtonCommand {

    private final CrmClient crmClient;
    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    @Override
    public void handle(CallbackQuery cb, AbsSender sender) {
        String data = cb.getData();
        Long appId = null;
        String selectedStatus = null;

        if (data.startsWith("OPERATED_PAID_")) {
            appId = parseId(data, "OPERATED_PAID_");
            selectedStatus = "PAID";
        } else if (data.startsWith("OPERATED_QUOTA_")) {
            appId = parseId(data, "OPERATED_QUOTA_");
            selectedStatus = "QUOTA";
        }

        if (appId == null || selectedStatus == null) return;

        try {
            ApplicationResponseDto app = crmClient.getApplication(appId);

            Long patientId = app.getPatient().getId();
            crmClient.updatePatientPaymentStatus(patientId, selectedStatus);

            InlineKeyboardMarkup markup = MenuFactory.updatedKeyboard(app);
            EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
            edit.setChatId(cb.getMessage().getChatId().toString());
            edit.setMessageId(cb.getMessage().getMessageId());
            edit.setReplyMarkup(markup);
            sender.execute(edit);

        } catch (Exception e) {
            log.error("Ошибка при изменении статуса операции: " + e.getMessage());
        }
    }

    private Long parseId(String data, String prefix) {
        try {
            return (data != null && data.startsWith(prefix))
                    ? Long.parseLong(data.substring(prefix.length()))
                    : null;
        } catch (Exception e) {
            log.error("Ошибка при разборе ID операции: " + e.getMessage());
            return null;
        }
    }
}