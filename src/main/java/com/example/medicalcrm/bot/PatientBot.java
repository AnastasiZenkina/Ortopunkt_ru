package com.example.medicalcrm.bot;
import com.example.medicalcrm.config.BotConfig;
import com.example.medicalcrm.entity.BotUser;
import com.example.medicalcrm.service.BotUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.generics.TelegramBot;


@Component
@RequiredArgsConstructor
public class PatientBot extends TelegramLongPollingBot  {
    private final BotConfig botConfig;
    private final BotUserService botUserService;

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();

            if(botUserService.getBotUserByTelegramId(chatId).isEmpty()){
                BotUser user = new BotUser();
                user.setTelegramId(chatId);
                user.setUsername(username);
                user.setRole("PATIENT");
                botUserService.saveBotUser(user);
            }

            if(update.getMessage().hasText()){
                String text = update.getMessage().getText();
                SendMessage message = new SendMessage();
                message.setChatId(chatId.toString());

                if(text.equalsIgnoreCase("/start")){
                    message.setText("Добро пожаловать, " + username + "! Чем могу помочь?");
                }
                else{
                    message.setText("Спасибо за сообщение! Мы с вами свяжемся 🧡");
                }

                try {
                    execute(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            else if(update.getMessage().hasPhoto()){
                SendMessage photoReply = new SendMessage();
                photoReply.setChatId(chatId.toString());
                photoReply.setText("Фото получено! Спасибо, мы передадим его врачу.");

                try {
                    execute(photoReply);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
