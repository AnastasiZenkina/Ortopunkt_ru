package com.ortopunkt.medicalcrm.config;
import com.ortopunkt.medicalcrm.bot.MainBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
public class TelegramBotInitializer {
    private final MainBot mainBot;

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException{
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(mainBot);
        return botsApi;
    }

}
