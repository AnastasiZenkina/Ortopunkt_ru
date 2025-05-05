package com.example.medicalcrm.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data

public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer patientId;
    private Integer campaignId;
    private String text;
    private String status;
    private String source;
    private String channel;
    private boolean targetOrSpam;
    private LocalDate createdAt;
    private String adType;
    private String creative;

}
