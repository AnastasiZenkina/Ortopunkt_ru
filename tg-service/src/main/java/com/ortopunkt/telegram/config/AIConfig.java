package com.ortopunkt.telegram.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ai")
@Getter
@Setter
public class AIConfig {
    private boolean enabled = true;
    private long delayMinutes = 30;
    private String url;
}