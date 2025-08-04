package com.ortopunkt.medicalcrm.bot.ai;

import com.ortopunkt.medicalcrm.entity.Application;
import com.ortopunkt.medicalcrm.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Slf4j
public class AiAutoReplySender {

    @Autowired
    private final AiHttpClient aiHttpClient;
    private final ApplicationService applicationService;
    private final AbsSender sender;

    public AiAutoReplySender(
            AiHttpClient aiHttpClient,
            ApplicationService applicationService,
            @Qualifier("botSender") AbsSender sender
    ) {
        this.aiHttpClient = aiHttpClient;
        this.applicationService = applicationService;
        this.sender = sender;
    }

    public void send(Application app) {
        String text = app.getText();

        if (!TextSanitizer.isSafe(text)) {
            log.warn("Обнаружены персональные данные — отправляем шаблон 'common'");
            SendMessage fallbackMessage = new SendMessage();
            fallbackMessage.setChatId(app.getPatient().getTgId());
            fallbackMessage.setText("Спасибо за сообщение! Врач посмотрит и ответит вам лично 🌿");
            try {
                sender.execute(fallbackMessage);
            } catch (Exception e) {
                log.error("Ошибка при отправке fallback-сообщения", e);
            }
            return;
        }

        String replyText = null;
        try {
            AiResponse response = aiHttpClient.getResponse(text, applicationService.hasPhotos(app));
            replyText = response != null ? response.getReply() : null;
        } catch (Exception e) {
            log.error("Ошибка при вызове AI", e);
        }

        if (replyText == null || replyText.isBlank()) {
            log.warn("AI не дал ответа — пациенту ничего не отправляем");
            return;
        }

        SendMessage message = new SendMessage();
        message.setChatId(app.getPatient().getTgId());
        message.setText("🤖 *Врач временно занят. Вам отвечает искусственный интеллект:*\n\n" + replyText);
        message.enableMarkdown(true);
        try {
            sender.execute(message);
        } catch (Exception e) {
            log.error("Ошибка при отправке AI-ответа", e);
            return;
        }

        app.setAnsweredByAi(true);
        applicationService.saveApplication(app);
    }
}
