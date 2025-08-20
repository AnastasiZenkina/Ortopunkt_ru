package com.ortopunkt.telegram.config;

import com.ortopunkt.telegram.ui.MainBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Configuration
public class BotSenderConfig {

    @Primary
    @Bean
    public AbsSender botSender(MainBot mainBot) {
        return mainBot;
    }
}
