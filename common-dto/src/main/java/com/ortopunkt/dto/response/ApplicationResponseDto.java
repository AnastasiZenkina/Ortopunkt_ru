package com.ortopunkt.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

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
    private String paymentStatus;
    private String source;
    private String channel;
    private boolean targetOrSpam;
    private LocalDate createdAt;
    private String adType;
    private String creative;
    private List<String> photoFileIds;

}
