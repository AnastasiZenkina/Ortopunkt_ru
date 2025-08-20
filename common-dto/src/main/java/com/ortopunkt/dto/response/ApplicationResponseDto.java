package com.ortopunkt.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data

public class ApplicationResponseDto {
    private Long id;
    private Long patientId;
    private PatientResponseDto patient;
    private boolean answeredByHuman;
    private boolean answeredByAi;
    private Long campaignId;
    private String text;
    private String status;
    private String source;
    private String channel;
    private boolean targetOrSpam;
    private LocalDate createdAt;
    private String adType;
    private String creative;

}
