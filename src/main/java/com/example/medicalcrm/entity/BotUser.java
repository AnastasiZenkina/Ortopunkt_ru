package com.example.medicalcrm.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class BotUser {
    @Id
    private Long telegramId;

    private String role;
    private String username;
}
