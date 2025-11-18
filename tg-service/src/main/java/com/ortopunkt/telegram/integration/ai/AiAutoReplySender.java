package com.ortopunkt.telegram.integration.ai;

import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.dto.response.AiResponse;
import com.ortopunkt.telegram.client.AiClient;
import com.ortopunkt.telegram.client.CrmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiAutoReplySender {

    private final AiClient aiClient;
    private final CrmClient crmClient;
    @Qualifier("botSender")
    private final AbsSender sender;

    private static final Set<Long> IN_PROGRESS = ConcurrentHashMap.newKeySet();

    public void send(ApplicationResponseDto app) {
        if (app == null) return;
        if (app.isAnsweredByAi()) return;
        Long appId = app.getId();
        if (!IN_PROGRESS.add(appId)) return;
        try {
            ApplicationResponseDto crmApp = crmClient.getApplication(appId);
            if (crmApp == null) return;
            if (Boolean.TRUE.equals(crmApp.isAnsweredByAi())) return;
            String text = crmApp.getText() != null ? crmApp.getText() : app.getText();
            if (crmApp.getPatient() == null) return;
            if (!TextSanitizer.isSafe(text)) {
                sendFallbackMessage(crmApp);
                return;
            }
            boolean hasPhotos = crmApp.getPhotoFileIds() != null && !crmApp.getPhotoFileIds().isEmpty();
            String replyText = getAiReply(text, hasPhotos);
            if (replyText == null || replyText.isBlank()) {
                log.warn("AI не дал ответа — пациенту ничего не отправляем");
                return;
            }
            sendAiReply(crmApp, replyText);
            markAsAnswered(crmApp);
        } catch (Exception e) {
            log.error("Ошибка в процессе отправки AI-ответа", e);
        } finally {
            IN_PROGRESS.remove(appId);
        }
    }

    private void sendFallbackMessage(ApplicationResponseDto app) {
        SendMessage message = new SendMessage();
        message.setChatId(app.getPatient().getTgId());
        message.setText("Спасибо за сообщение! Врач посмотрит и ответит вам лично.");
        try {
            sender.execute(message);
        } catch (Exception e) {
            log.error("Ошибка при отправке fallback-сообщения", e);
        }
    }

    private String getAiReply(String text, boolean hasPhotos) {
        try {
            AiResponse response = aiClient.getResponse(text, hasPhotos);
            return response != null ? response.getReply() : null;
        } catch (Exception e) {
            log.error("Ошибка при вызове AI", e);
            return null;
        }
    }

    private void sendAiReply(ApplicationResponseDto app, String replyText) {
        if (app.getPatient() == null) return;
        SendMessage message = new SendMessage();
        message.setChatId(app.getPatient().getTgId());
        message.setText("Врач временно занят. Вам отвечает искусственный интеллект:\n\n" + replyText);
        message.enableMarkdown(true);
        try {
            sender.execute(message);
        } catch (Exception e) {
            log.error("Ошибка при отправке AI-ответа", e);
        }
    }

    private void markAsAnswered(ApplicationResponseDto app) {
        try {
            app.setAnsweredByAi(true);
            crmClient.updateApplicationStatus(app.getId(), "AI_ANSWERED");
        } catch (Exception e) {
            log.error("Ошибка при сохранении статуса answeredByAi", e);
        }
    }
}