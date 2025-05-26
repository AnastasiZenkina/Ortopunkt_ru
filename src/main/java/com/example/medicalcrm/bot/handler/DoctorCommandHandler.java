package com.example.medicalcrm.bot.handler;

import com.example.medicalcrm.entity.Application;
import com.example.medicalcrm.service.ApplicationService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
public class DoctorCommandHandler {

    public void handle(Update update, AbsSender sender, ApplicationService applicationService) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());

        if (text.equalsIgnoreCase("/отчёт") || text.equalsIgnoreCase("/отчет")) {
            message.setText("🧠 Еженедельный отчёт:\n(Тут будет аналитика — пока заглушка)");
        } else if (text.equalsIgnoreCase("/пациенты")) {
            List<Application> applications = applicationService.getAllApplications();
            StringBuilder response = new StringBuilder("📋 Список новых заявок:\n\n");

            if (applications.isEmpty()) {
                response.append("Нет новых заявок.");
            } else {
                for (Application app : applications) {
                    response.append("Пациент: ")
                            .append(app.getPatient() != null ? app.getPatient().getName() : "неизвестно")
                            .append("\nЗаявка: ")
                            .append(app.getText())
                            .append("\n\n");
                }
            }

            message.setText(response.toString());
        } else {
            message.setText("Здравствуйте, доктор! Используйте /отчёт или /пациенты.");
        }

        try {
            sender.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
