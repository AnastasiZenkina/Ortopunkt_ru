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
public class AnswerCommand implements ButtonCommand {

    private final CrmClient crmClient;
    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    @Override
    public void handle(CallbackQuery query, AbsSender sender) {
        Long appId = parseId(query.getData(), "ANSWER_");
        if (appId == null) return;

        try {
            ApplicationResponseDto currentApp = crmClient.getApplication(appId);
            String newStatus = "ANSWERED".equals(currentApp.getStatus()) ? "NEW" : "ANSWERED";
            crmClient.updateApplicationStatus(appId, newStatus);

            ApplicationResponseDto updatedApp = crmClient.getApplication(appId);
            InlineKeyboardMarkup updatedMarkup = MenuFactory.updatedKeyboard(updatedApp);

            EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
            editMarkup.setChatId(query.getMessage().getChatId().toString());
            editMarkup.setMessageId(query.getMessage().getMessageId());
            editMarkup.setReplyMarkup(updatedMarkup);
            sender.execute(editMarkup);

        } catch (Exception e) {
            log.error("Ошибка при обновлении кнопок после ответа: " + e.getMessage());
        }
    }

    private Long parseId(String data, String prefix) {
        try {
            return (data != null && data.startsWith(prefix))
                    ? Long.parseLong(data.substring(prefix.length()))
                    : null;
        } catch (Exception e) {
            log.error("Ошибка при разборе ID из callback: " + e.getMessage());
            return null;
        }
    }
}