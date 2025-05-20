package com.example.medicalcrm.bot;

import com.example.medicalcrm.config.BotConfig;
import com.example.medicalcrm.entity.BotUser;
import com.example.medicalcrm.service.BotUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component("mainBot")
@RequiredArgsConstructor
public class MainBot extends TelegramLongPollingBot {

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
            System.out.println("chatId: " + chatId + ", username: " + username);


            if (botUserService.getBotUserByTelegramId(chatId).isEmpty()) {
                BotUser user = new BotUser();
                user.setTelegramId(chatId);
                user.setUsername(username);
                user.setRole("PATIENT");
                botUserService.saveBotUser(user);
            }

            String role = botUserService.getBotUserByTelegramId(chatId)
                    .map(BotUser::getRole)
                    .orElse("PATIENT");

            if (update.getMessage().hasText()) {
                String text = update.getMessage().getText();
                SendMessage message = new SendMessage();
                message.setChatId(chatId.toString());

                if (role.equals("DOCTOR")) {
                    if (text.equalsIgnoreCase("/отчёт") || text.equalsIgnoreCase("/отчет")) {
                        message.setText("🧠 Еженедельный отчёт:\n(Тут будет аналитика — пока заглушка)");
                    } else if (text.equalsIgnoreCase("/аналитика")) {
                        message.setText("📊 Подробная аналитика:\n(Тут будет статистика — пока заглушка)");
                    } else {
                        message.setText("Здравствуйте, доктор! Используйте /отчёт или /аналитика.");
                    }

                } else if (role.equals("SMM")) {
                    if (text.equalsIgnoreCase("/smm") || text.equalsIgnoreCase("/смм")) {
                        message.setText("""
                📊 SMM-отчёт за неделю

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
                """);
                    } else {
                        message.setText("Здравствуйте. Используйте команду /smm для получения отчёта.");
                    }

                } else {
                    if (text.equalsIgnoreCase("/start")) {
                        message.setText("Добро пожаловать, " + username + "! Чем могу помочь?");
                    } else {
                        message.setText("Спасибо за сообщение! Мы с вами свяжемся 🧡");
                    }
                }

                try {
                    execute(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (update.getMessage().hasPhoto()) {
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
