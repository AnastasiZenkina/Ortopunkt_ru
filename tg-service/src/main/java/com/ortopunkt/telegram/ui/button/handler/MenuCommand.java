package com.ortopunkt.telegram.ui.button.handler;

import com.ortopunkt.telegram.ui.button.ButtonCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class MenuCommand implements ButtonCommand {

    @Override
    public void handle(CallbackQuery cb, AbsSender sender) {
        String data = cb.getData();
        String chatId = cb.getMessage().getChatId().toString();
        String text;

        switch (data) {
            case "DOCTOR_REPORT" -> text = doctorReport();
            case "SMM_REPORT" -> text = smmReport();
            case "DOCTOR_PATIENTS" -> text = patientList();
            case "TARGET_REPORT" -> text = targetReport();
            default -> text = "❓ Неизвестная команда.";
        }

        SendMessage message = new SendMessage(chatId, text);
        message.enableHtml(true);

        try {
            sender.execute(message);
        } catch (Exception e) {
            // логирование идёт через GlobalExceptionHandler
            com.ortopunkt.logging.GlobalExceptionHandler.logError(e);
        }
    }

    private String doctorReport() {
        return """
                <b>🧠 Отчёт за апрель</b>
                (здесь остаётся твой статический пример)
                """;
    }

    private String smmReport() {
        return """
                📊 <b>SMM-отчёт за неделю</b>
                (здесь остаётся твой статический пример)
                """;
    }

    private String targetReport() {
        return """
                📈 <b>Таргет-отчёт</b>
                Эта функция пока в разработке.
                🛠️
                """;
    }

    private String patientList() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            // ⚡ обращаемся к analytics-service
            return restTemplate.getForObject(
                    "http://localhost:8084/api/analytics/applications",
                    String.class
            );
        } catch (Exception e) {
            com.ortopunkt.logging.GlobalExceptionHandler.logError(e);
            return "⚠️ Ошибка: не удалось получить данные аналитики.";
        }
    }
}