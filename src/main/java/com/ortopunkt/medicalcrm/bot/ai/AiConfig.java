package com.ortopunkt.medicalcrm.bot.ai;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ai")
@Getter
@Setter
public class AiConfig {
    private boolean enabled = true;
    private long delayMinutes = 30;

    private String url = "http://localhost:8081/api/ai/response";
}
