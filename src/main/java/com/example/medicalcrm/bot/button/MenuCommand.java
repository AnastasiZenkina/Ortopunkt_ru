package com.example.medicalcrm.bot.button;

import com.example.medicalcrm.entity.Application;
import com.example.medicalcrm.entity.Patient;
import com.example.medicalcrm.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import com.example.medicalcrm.bot.button.ButtonCommand;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MenuCommand implements ButtonCommand {

    private final ApplicationService applicationService;

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
            e.printStackTrace();
        }
    }

    private String doctorReport() {
        return """
                <b>🧠 Отчёт за апрель</b>

                📣 <b>Реклама (Таргет):</b>
                — Подписки: 142
                — Сообщений: 26
                — Записались: 9
                💸 Цена клика: 19.3₽
                👁️ Охват: 6 820

                🌱 <b>Органика:</b>
                — Подписки: 78
                — Сообщений: 12
                — Записались: 4
                👁️ Охват: 1 240

                📌 <b>Топ-посты:</b>
                VK: “Пациентка из Екатеринбурга — фото до/после”
                https://vk.com/...
                Insta: “Отзыв о биовинтах” — см. пост от 12.04
                """;
    }

    private String smmReport() {
        return """
                📊 <b>SMM-отчёт за неделю</b>

                👥 VK:
                👁️ Охват: 6 120
                🤝 Вовлечённость: 4.2%
                📌 Подписки: 84
                🔁 Репостов: 28
                💬 Комментарии: 19

                📷 Instagram:
                👁️ Охват: 3 420
                🤝 Вовлечённость: 5.1%
                📌 Подписки: 44
                🔁 Репостов: 14
                💬 Комментарии: 22

                📉 Слабые посты:
                VK:  “Плоскостопие” — 230 охвата, 1 лайк
                Insta: “Вальгус у детей” — 260 охвата, 1 заявка
                """;
    }

    private String targetReport() {
        return """
                📈 <b>Таргет-отчёт</b>

                Эта функция пока в разработке.
                Позже здесь будет подробный отчёт по рекламным кампаниям.
                🛠️
                """;
    }

    private String patientList() {
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        List<Application> recent = applicationService.getAllApplications().stream()
                .filter(app -> app.getCreatedAt() != null && !app.getCreatedAt().isBefore(weekAgo))
                .toList();

        Set<String> tgIds = recent.stream()
                .map(app -> app.getPatient() != null ? app.getPatient().getTgId() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        StringBuilder sb = new StringBuilder();
        sb.append("📝 За <b>неделю</b> было <b>")
                .append(tgIds.size())
                .append("</b> заявок:\n\n");

        Set<String> seen = new HashSet<>();
        for (Application app : recent) {
            Patient p = app.getPatient();
            if (p != null) {
                String id = p.getTgId();
                if (id != null && seen.add(id)) {
                    String name = p.getName() != null ? p.getName() : "";
                    String username = p.getUsername() != null ? "(@" + p.getUsername() + ")" : "";
                    sb.append("• ").append(name).append(" ").append(username).append("\n");
                }
            }
        }
        return sb.toString();
    }
}
