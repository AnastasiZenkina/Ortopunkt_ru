package com.example.medicalcrm.dto;
import lombok.Data;
import java.time.LocalDate;

@Data

public class BotUserResponseDto {
    private Long telegramId;

    private String role;
    private String username;

}
