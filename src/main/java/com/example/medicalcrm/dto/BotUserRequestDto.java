package com.example.medicalcrm.dto;
import lombok.Data;
import java.time.LocalDate;

@Data

public class BotUserRequestDto {
    private String role;
    private String username;
}
