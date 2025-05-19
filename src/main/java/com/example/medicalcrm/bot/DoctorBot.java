package com.example.medicalcrm.bot;
import com.example.medicalcrm.config.DoctorConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class DoctorBot extends TelegramLongPollingBot{
    private final DoctorConfig doctorConfig;

    @Override
    public String getBotUsername(){
        return doctorConfig.getBotUsername();
    }

    @Override
    public String getBotToken(){
        return doctorConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update){
        if(update.hasMessage() && update.getMessage().hasText()){
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            SendMessage message = new SendMessage();
            message.setChatId(chatId.toString());

            if(text.equalsIgnoreCase("/отчёт") || text.equalsIgnoreCase("/отчет")){
                message.setText("🧠 Еженедельный отчёт:\n(Тут будет аналитика — пока заглушка)");
            }
            else if(text.equalsIgnoreCase("/аналитика")){
                message.setText("📊 Подробная аналитика:\n(Тут будет статистика — пока заглушка)");
            }
            else{
                message.setText("Здравствуйте! Я врач-бот. Введите команду /отчёт или /аналитика.");
            }

            try{
                execute(message);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
