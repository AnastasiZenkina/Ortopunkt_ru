package com.ortopunkt.telegram.aiintegration;

import com.ortopunkt.crm.service.ApplicationService;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.dto.response.AiResponse;
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

    public void send(ApplicationResponseDto app) {
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
            // теперь проверка фото — через dto
            AiResponse response = aiHttpClient.getResponse(text, applicationService.hasPhotos(
                    applicationService.getApplication(app.getId())
            ));
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

        // теперь через id вытаскиваем entity и сохраняем
        applicationService.getApplicationById(app.getId()).ifPresent(entity -> {
            entity.setAnsweredByAi(true);
            applicationService.saveApplication(entity);
        });
    }
}
