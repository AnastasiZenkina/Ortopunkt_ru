package com.example.medicalcrm.dto;
import lombok.Data;
import java.time.LocalDate;

@Data

public class ApplicationRequestDto {
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
