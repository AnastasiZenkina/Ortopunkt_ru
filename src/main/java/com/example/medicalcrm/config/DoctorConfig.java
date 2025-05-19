package com.example.medicalcrm.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "doctorbot")
public class DoctorConfig {
    private String botUsername;
    private String botToken;
}
