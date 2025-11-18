package com.ortopunkt.analyticsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling   // планировщик
@EnableCaching      // кэширование
@EnableAsync        // асинхронность
@SpringBootApplication(scanBasePackages = {
        "com.ortopunkt.analyticsservice",
        "com.ortopunkt.config"
})
public class AnalystsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalystsServiceApplication.class, args);
    }
}