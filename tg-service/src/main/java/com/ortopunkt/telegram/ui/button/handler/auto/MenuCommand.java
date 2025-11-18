package com.ortopunkt.telegram.ui.button.handler.auto;

import com.ortopunkt.logging.ServiceLogger;
import com.ortopunkt.telegram.integration.social.ReportService;
import com.ortopunkt.telegram.ui.button.ButtonCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class MenuCommand implements ButtonCommand {

    private final ReportService reportService;
    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    @Override
    public void handle(CallbackQuery cb, AbsSender sender) {
        String data = cb.getData();
        String chatId = cb.getMessage().getChatId().toString();
        String text;

        switch (data) {
            case "DOCTOR_REPORT" -> text = reportService.buildDoctorReport();
            case "SMM_REPORT" -> text = reportService.buildSmmReport();
            case "TARGET_REPORT" -> text = reportService.buildTargetReport();
            case "DOCTOR_PATIENTS" -> text = reportService.buildPatientList();
            default -> text = "Неизвестная команда.";
        }

        SendMessage message = new SendMessage(chatId, text);
        message.enableHtml(true);

        try {
            sender.execute(message);
        } catch (Exception e) {
            log.error("Ошибка при отправке отчёта: " + e.getMessage());
        }
    }
}