package com.ortopunkt.telegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.ortopunkt.telegram",
        "com.ortopunkt.crm"
})

@EnableScheduling
public class TgServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TgServiceApplication.class, args);
    }

}
