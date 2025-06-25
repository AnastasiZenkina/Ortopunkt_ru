package com.ortopunkt.medicalcrm.config;

import com.ortopunkt.medicalcrm.bot.MainBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Configuration
public class BotSenderConfig {

    @Bean
    public AbsSender botSender(MainBot mainBot) {
        return mainBot;
    }
}
