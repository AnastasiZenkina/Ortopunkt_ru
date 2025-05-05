package com.example.medicalcrm.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class BotUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;
    private String username;
}
